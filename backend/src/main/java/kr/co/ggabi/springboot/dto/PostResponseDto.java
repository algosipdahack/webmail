package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.domain.posts.PostList;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private Long board_id;
    private Long writer_id;
    private String content;
    private List<Attachment> attachment;
    private List<Comment> comment;
    private PostList list;

    public PostResponseDto(Post entity) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.board_id = entity.getBoard_id();
        this.writer_id = entity.getWriter_id();
        this.attachment = entity.getAttachment();
        this.comment = entity.getComment();
        this.list = entity.getList();
    }
}
