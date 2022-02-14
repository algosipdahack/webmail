package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.comments.Comment;
import lombok.Getter;

@Getter
public class CommentListResponseDto {
    private Long id;
    private Long parent_id;
    private String content;
    private String writer;
    private String writerName;

    public CommentListResponseDto(Comment entity) {
        this.id = entity.getId();
        this.parent_id = entity.getParentId();
        this.content = entity.getContent();
        this.writer = entity.getWriter();
        this.writerName = entity.getWriterName();
    }
}
