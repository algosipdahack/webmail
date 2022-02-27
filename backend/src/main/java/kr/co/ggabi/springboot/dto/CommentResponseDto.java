package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.comments.Comment;
import lombok.Getter;

import java.util.Date;

@Getter
public class CommentResponseDto {
    private Long id;
    private Long parentId;
    private String content;
    private String writer;
    private String writerName;
    public Date isCreated;

    public CommentResponseDto(Comment entity) {
        this.id = entity.getId();
        this.parentId = entity.getParentId();
        this.content = entity.getContent();
        this.writer = entity.getWriter();
        this.writerName = entity.getWriterName();
        this.isCreated = entity.getIsCreated();
    }
}
