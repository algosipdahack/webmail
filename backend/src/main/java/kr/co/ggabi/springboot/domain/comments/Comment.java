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
    private Long post_id;
    @Column
    private Long parent_id;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @Column
    private String writer; // username

    @Column
    private Long writer_id;

    @Builder // 빌더 형태로 만들어줌
    public Comment(Long parent_id, String content, String writer, Long writer_id,Long post_id) {//생성자
        this.parent_id = parent_id;
        this.content = content;
        this.writer = writer;
        this.writer_id = writer_id;
        this.post_id = post_id;
    }

    public void update(String content) {
        this.content = content;
    }
}
