package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.comments.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private Long parent_id;
    private String content;
    private String writer;

    public CommentResponseDto(Comment entity) {
        this.id = entity.getId();
        this.parent_id = entity.getParent_id();
        this.content = entity.getContent();
        this.writer = entity.getWriter();
    }
}
