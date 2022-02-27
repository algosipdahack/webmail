package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.dto.*;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.service.*;
import kr.co.ggabi.springboot.util.MD5Generator;
import lombok.RequiredArgsConstructor;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class PostApiController {
    private final PostService postService;
    private final PostListService postListService;
    private final AttachmentService attachmentService;
    private final BoardService boardService;
    private final TokenProvider tokenProvider;
    private final FileDownloadService service;

    //전체 게시판 목록 출력
    @GetMapping("/post")
    public List<PostListResponseDto> readAll() {
        return postListService.findAllDesc();
    }

    //create a post
    @PostMapping("/{bid}")
    public Long save(@PathVariable("bid") Long bid, @RequestParam("content") String content, @RequestParam("writer") String writer, @RequestParam("title") String title, @RequestParam("is_notice") boolean is_notice, @RequestParam("file") List<MultipartFile> files) {
        try {
            PostSaveRequestDto requestDto = new PostSaveRequestDto(content);
            requestDto.setBoardId(bid);

            PostListSaveRequestDto requestDto_list = new PostListSaveRequestDto(writer, title, is_notice);
            Long postlistId = postListService.save(requestDto_list).getId();
            requestDto.setPostlistId(postlistId);

            //BOARD에서도 저장해줘야함
            boardService.save_postlist(bid, postlistId);

            List<Long> attachments = new ArrayList<>();
            if (!files.isEmpty()) {
                for (MultipartFile file : files) {
                    String origFilename = file.getOriginalFilename();
                    String filename = new MD5Generator(origFilename).toString();
                    String filePath = System.getProperty("user.dir") + File.separator + "downloads" + File.separator + "board" + File.separator + Long.toString(bid) + File.separator + Long.toString(postlistId) + File.separator + filename;
                    System.out.println(filePath);
                    File downloads = new File("./downloads");
                    downloads.mkdirs();
                    File mailBoxDir = new File("./downloads" + File.separator + "board");
                    mailBoxDir.mkdirs();
                    File uidDir = new File("./downloads" + File.separator + "board" + File.separator + Long.toString(bid));
                    uidDir.mkdirs();
                    File idxDir = new File("./downloads" + File.separator + "board" + File.separator + Long.toString(bid) + File.separator + Long.toString(postlistId));
                    idxDir.mkdirs();


                    file.transferTo(new File(filePath));
                    String link = "/api/board/download/" + Long.toString(bid) + "/" + Long.toString(postlistId) + "/" + filename;

                    AttachmentDto fileDto = new AttachmentDto();
                    fileDto.setOrigFilename(origFilename);
                    fileDto.setFilename(filename);
                    fileDto.setFilePath(link);
                    fileDto.setSize(file.getSize());
                    attachments.add(attachmentService.saveFile(fileDto).getId());
                }
            }
            requestDto.setAttachmentId(attachments);
            Long postID = postService.save(requestDto).getId();
            postListService.savePostId(postID);
            return postID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //modify post
    @PutMapping("/{bid}/{pid}")
    public Long update(@PathVariable("bid") Long bid, @PathVariable("pid") Long pid, @RequestParam("file") List<MultipartFile> files, @RequestParam("content") String content, @RequestParam("title") String title, @RequestParam("is_notice") boolean is_notice) {
        PostUpdateRequestDto requestDto = new PostUpdateRequestDto();
        requestDto.setContent(content);

        PostListUpdateRequestDto requestDto_list = new PostListUpdateRequestDto();
        requestDto_list.setTitle(title);
        requestDto_list.setIs_notice(is_notice);

        Long postlistId = postListService.update(bid, pid, requestDto_list).getId();
        //해당 게시물의 첨부파일 일단 모두 지움
        postService.delete_file(pid);
        //다시 첨부파일 저장
        try {
            List<Long> attachments = new ArrayList<>();
            if (!files.isEmpty()) {
                for (MultipartFile file : files) {
                    String filePath = System.getProperty("user.dir") + File.separator + "downloads" + File.separator + "board" + File.separator + Long.toString(bid) + File.separator + Long.toString(postlistId) + File.separator + file;
                    System.out.println(filePath);
                    File downloads = new File("./downloads");
                    downloads.mkdirs();
                    File mailBoxDir = new File("./downloads" + File.separator + "board");
                    mailBoxDir.mkdirs();
                    File uidDir = new File("./downloads" + File.separator + "board" + File.separator + Long.toString(bid));
                    uidDir.mkdirs();
                    File idxDir = new File("./downloads" + File.separator + "board" + File.separator + Long.toString(bid) + File.separator + Long.toString(postlistId));
                    idxDir.mkdirs();
                    String origFilename = file.getOriginalFilename();
                    String filename = new MD5Generator(origFilename).toString();
                    file.transferTo(new File(filePath, filename));
                    System.out.println("Transfer");
                    String link = "/api/board/download/" + Long.toString(bid) + "/" + Long.toString(postlistId) + "/" + filename;

                    AttachmentDto fileDto = new AttachmentDto();
                    fileDto.setOrigFilename(origFilename);
                    fileDto.setFilename(filename);
                    fileDto.setFilePath(link);
                    fileDto.setSize(file.getSize());
                    attachments.add(attachmentService.saveFile(fileDto).getId());
                }
            }
            requestDto.setAttachmentId(attachments);
            return postService.update(bid, pid, requestDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //return specific post information
    @GetMapping("/{bid}/{pid}")
    public PostResponseDto findById(@PathVariable("bid") Long bid, @PathVariable("pid") Long pid) {
        return postService.findById(bid, pid);
    }

    //remove post
    @DeleteMapping("/{bid}/{pid}")
    public void delete(@PathVariable("bid") Long bid, @PathVariable("pid") Long pid) {
        //postlist, board에서도 지워야 함
        postService.delete(bid, pid);
    }

    @GetMapping("/download/{bid}/{postlistId}/{filename:.+}")
    public ResponseEntity<InputStreamResource> downloadFile(HttpServletResponse response, @PathVariable("bid") Long bid, @PathVariable("postlistId") Long id, @PathVariable("filename") String filename) throws IOException {
        String filePath = System.getProperty("user.dir") + File.separator + "downloads" + File.separator + "board" + File.separator + Long.toString(bid) + File.separator + Long.toString(id) + File.separator + filename;
        File file = new File(filePath);
        String fileName = file.getName();
        // 파일 확장자
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        HttpHeaders header = new HttpHeaders();
        Path fPath = Paths.get(file.getAbsolutePath());
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        InputStreamResource resource3 = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource3);
    }
}