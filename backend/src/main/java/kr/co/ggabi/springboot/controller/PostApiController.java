package kr.co.ggabi.springboot.controller;
import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.dto.*;
import kr.co.ggabi.springboot.service.AttachmentService;
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
    private final AttachmentService attachmentService;

    //전체 게시판 목록 출력
    @GetMapping("/post")
    public List<PostListResponseDto> readAll() {
        return postService.findAllDesc();
    }

    //create a post
    @PostMapping("/{bid}")
    public Post save(@PathVariable("bid") Long bid, @RequestParam("file") List<MultipartFile> files, @RequestBody PostSaveRequestDto requestDto) {
        try {
            List<Attachment> attachments = new ArrayList<>();
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
                    attachments.add(attachmentService.saveFile(fileDto));
                }
            }
            requestDto.setAttachment(attachments);
            requestDto.setBoard_id(bid);
            return postService.save(requestDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //modify post
    @PutMapping("/{bid}/{pid}")
    public Post update(@PathVariable("bid") Long bid, @PathVariable("pid") Long pid, @RequestParam("file") List<MultipartFile> files, @RequestBody PostUpdateRequestDto requestDto) {
        //해당 게시물의 첨부파일 일단 모두 지움
        postService.delete_file(pid);
        //다시 첨부파일 저장
        try {
            List<Attachment> attachments = new ArrayList<>();
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

                    attachments.add(attachmentService.saveFile(fileDto));
                }
            }
            requestDto.setAttachment(attachments);
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