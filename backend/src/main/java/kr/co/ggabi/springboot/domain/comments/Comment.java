package kr.co.ggabi.springboot.domain.comments;
import kr.co.ggabi.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class Comment {
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
    private String writerName;

    @CreationTimestamp
    private Date isCreated;

    @Builder // 빌더 형태로 만들어줌
    public Comment(Long parentId, String content, String writer, String writerName,Long postId) {//생성자
        this.parentId = parentId;
        this.content = content;
        this.writer = writer;
        this.writerName = writerName;
        this.postId = postId;
    }

    public void update(String content) {
        this.content = content;
    }
}
