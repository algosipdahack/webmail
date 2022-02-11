package kr.co.ggabi.springboot.controller;
import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.dto.*;
import kr.co.ggabi.springboot.service.AttachmentService;
import kr.co.ggabi.springboot.service.PostListService;
import kr.co.ggabi.springboot.service.PostService;
import kr.co.ggabi.springboot.util.MD5Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class PostApiController {
    private final PostService postService;
    private final PostListService postListService;
    private final AttachmentService attachmentService;

    //전체 게시판 목록 출력
    @GetMapping("/post")
    public List<PostListResponseDto> readAll() {

        return postListService.findAllDesc();
    }

    //create a post
    @PostMapping("/{bid}")
    public Long save(@PathVariable("bid") Long bid, @RequestParam("content") String content,@RequestParam("writer") String writer, @RequestParam("title") String title, @RequestParam("is_notice") boolean is_notice, @RequestParam("file") List<MultipartFile> files) {
        try {
            //board
            PostSaveRequestDto requestDto = new PostSaveRequestDto();
            requestDto.setContent(content);
            requestDto.setBoardId(bid);

            PostListSaveRequestDto requestDto_list = new PostListSaveRequestDto();
            requestDto_list.setWriter(writer);
            requestDto_list.setTitle(title);
            requestDto_list.setIs_notice(is_notice);

            Long postlistId = postListService.save(requestDto_list).getId();
            requestDto.setPostlistId(postlistId);

            List<Long> attachments = new ArrayList<>();
            if(!files.isEmpty()){
                /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
                String savePath = System.getProperty("user.dir") + "\\files";
                /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
                if (!new File(savePath).exists()) {
                    try{
                        new File(savePath).mkdir();
                    }
                    catch(Exception e){
                        e.getStackTrace();
                    }
                }
                for(MultipartFile file: files) {
                    String origFilename = file.getOriginalFilename();
                    String filename = new MD5Generator(origFilename).toString();
                    String filePath = savePath + "\\" + filename;
                    file.transferTo(new File(filePath));

                    AttachmentDto fileDto = new AttachmentDto();
                    fileDto.setOrigFilename(origFilename);
                    fileDto.setFilename(filename);
                    fileDto.setFilePath(filePath);
                    fileDto.setSize(file.getSize());
                    attachments.add(attachmentService.saveFile(fileDto).getId());
                }
            }
            requestDto.setAttachmentId(attachments);
            return postService.save(requestDto).getId();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //modify post
    @PutMapping("/{bid}/{pid}")
    public Long update(@PathVariable("bid") Long bid, @PathVariable("pid") Long pid, @RequestParam("file") List<MultipartFile> files,@RequestParam("content") String content, @RequestParam("title") String title, @RequestParam("is_notice") boolean is_notice) {
        PostUpdateRequestDto requestDto = new PostUpdateRequestDto();
        requestDto.setContent(content);

        PostListUpdateRequestDto requestDto_list = new PostListUpdateRequestDto();
        requestDto_list.setTitle(title);
        requestDto_list.setIs_notice(is_notice);

        postListService.update(bid,pid,requestDto_list);
        //해당 게시물의 첨부파일 일단 모두 지움
        postService.delete_file(pid);
        //다시 첨부파일 저장
        try {
            List<Long> attachments = new ArrayList<>();
            if(!files.isEmpty()){
                /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
                String savePath = System.getProperty("user.dir") + "\\files";
                /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
                if (!new File(savePath).exists()) {
                    try{
                        new File(savePath).mkdir();
                    }
                    catch(Exception e){
                        e.getStackTrace();
                    }
                }
                for(MultipartFile file: files) {
                    String origFilename = file.getOriginalFilename();
                    String filename = new MD5Generator(origFilename).toString();
                    String filePath = savePath + "\\" + filename;
                    file.transferTo(new File(filePath));

                    AttachmentDto fileDto = new AttachmentDto();
                    fileDto.setOrigFilename(origFilename);
                    fileDto.setFilename(filename);
                    fileDto.setFilePath(filePath);
                    fileDto.setSize(file.getSize());

                    attachments.add(attachmentService.saveFile(fileDto).getId());
                }
            }
            requestDto.setAttachmentId(attachments);
            return postService.update(bid, pid, requestDto);
        } catch(Exception e) {
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
        postService.delete(bid, pid);
    }
}