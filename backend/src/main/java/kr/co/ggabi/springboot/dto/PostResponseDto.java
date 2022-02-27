package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.domain.posts.PostList;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private Long boardId;
    private String content;
    private List<Attachment> attachment;
    private List<Comment> comment;
    private PostList postlist;

    public PostResponseDto(Post entity) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.boardId = entity.getBoardId();
    }
}
