package kr.co.ggabi.springboot.domain.posts;
import kr.co.ggabi.springboot.domain.BaseTimeEntity;
import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.comments.Comment;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor // 파라미터 없는 기본생성자 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 생성
@Builder
@Entity
public class Post {
    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincrement
    private Long id;

    @Column(nullable = false)
    private Long postlistId;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @Column
    private Long boardId;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Long> attachmentId = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Long> commentId = new ArrayList<>();

     // 빌더 형태로 만들어줌
    public Post(Long postlistId, Long boardId,String content, List<Long> attachmentId, List<Long> commentId) {//생성자
        this.boardId = boardId;
        this.postlistId = postlistId;
        this.content = content;
        this.commentId = commentId;
        this.attachmentId = attachmentId;
    }

    public void update(String content,List<Long> attachmentId) {
        this.content = content;
        this.attachmentId = attachmentId;
    }
    public void update_comment(List<Long> commentId) {
        this.commentId = commentId;
    }

}
