package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.domain.posts.PostList;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private Long boardId;
    private String content;
    private List<Long> attachmentId;
    private List<Long> commentId;
    private Long postlistId;

    public PostResponseDto(Post entity) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.boardId = entity.getBoardId();
        this.attachmentId = entity.getAttachmentId();
        this.commentId = entity.getCommentId();
        this.postlistId = entity.getPostlistId();
    }
}
