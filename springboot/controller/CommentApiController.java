package kr.co.ggabi.springboot.controller;
import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.dto.CommentSaveRequestDto;
import kr.co.ggabi.springboot.dto.CommentUpdateRequestDto;
import kr.co.ggabi.springboot.service.CommentService;
import kr.co.ggabi.springboot.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class CommentApiController {
    private final CommentService commentService;
    private final PostService postService;
    //create a comment
    @PostMapping("/{bid}/{pid}")
    public Long save(@PathVariable("bid") Long bid, @PathVariable("pid") Long pid, @RequestBody CommentSaveRequestDto requestDto) {
        requestDto.setPostId(pid);
        Long id = commentService.save(requestDto).getId();
        //post에서도 추가시켜줘야 함
        postService.save_comment(pid, id);
        return id;
    }

    //modify comment
    @PutMapping("/{bid}/{pid}/{cid}")
    public Long update(@PathVariable("bid") Long bid, @PathVariable("pid") Long pid, @PathVariable("cid") Long cid, @RequestBody CommentUpdateRequestDto requestDto) {
        return commentService.update(bid,pid,cid,requestDto);
    }

    //remove comment
    @DeleteMapping("/{bid}/{pid}/{cid}")
    public void delete(@PathVariable("bid") Long bid, @PathVariable("pid") Long pid, @PathVariable("cid") Long cid){
        commentService.delete(bid,pid,cid);
    }
}
