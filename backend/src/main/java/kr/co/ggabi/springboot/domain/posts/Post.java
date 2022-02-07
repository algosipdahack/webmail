package kr.co.ggabi.springboot.domain.posts;
import kr.co.ggabi.springboot.domain.BaseTimeEntity;
import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.comments.Comment;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor // 파라미터 없는 기본생성자 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 생성
@Builder
@Entity
public class Post extends BaseTimeEntity {
    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincrement
    private Long id;

    @Column(nullable = false)
    private PostList list;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @Column
    private Long board_id;

    @Column
    private Long writer_id;

    @Builder.Default
    @Column
    private List<Attachment> attachment = new ArrayList<>();

    @Builder.Default
    @Column
    private List<Comment> comment = new ArrayList<>();

     // 빌더 형태로 만들어줌
    public Post(PostList list, Long board_id,String content, Long writer_id,List<Attachment> attachment, List<Comment> comment) {//생성자
        this.board_id = board_id;
        this.list = list;
        this.content = content;
        this.writer_id = writer_id;
        this.comment = comment;
        this.attachment = attachment;
    }

    public void update(PostList list, String content,List<Attachment> attachment) {
        this.list = list;
        this.content = content;
        this.attachment = attachment;
    }
    public void update_comment(List<Comment> comment) {
        this.comment = comment;
    }

}
