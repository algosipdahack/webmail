package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.comments.Comment;
import lombok.Getter;

@Getter
public class CommentListResponseDto {
    private Long id;
    private Long parent_id;
    private String content;
    private String writer;
    private Long writerId;

    public CommentListResponseDto(Comment entity) {
        this.id = entity.getId();
        this.parent_id = entity.getParentId();
        this.content = entity.getContent();
        this.writer = entity.getWriter();
        this.writerId = entity.getWriterId();
    }
}
