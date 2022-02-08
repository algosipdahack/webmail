package kr.co.ggabi.springboot.domain.comments;
import kr.co.ggabi.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends BaseTimeEntity {
    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincrement
    private Long id;
    @Column
    private Long postId;
    @Column
    private Long parentId;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @Column
    private String writer; // username

    @Column
    private Long writerId;

    @Builder // 빌더 형태로 만들어줌
    public Comment(Long parentId, String content, String writer, Long writerId,Long postId) {//생성자
        this.parentId = parentId;
        this.content = content;
        this.writer = writer;
        this.writerId = writerId;
        this.postId = postId;
    }

    public void update(String content) {
        this.content = content;
    }
}
