package kr.co.ggabi.springboot.domain.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor // 파라미터 없는 기본생성자 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 생성
@Builder
@Entity
public class PostList {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincrement
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Builder.Default
    @Column
    private Long postId = Long.valueOf(0);

    @Column
    private String writer;

    @Column
    private String writerName;

    @Builder.Default
    @Column(nullable = false)
    private Boolean is_notice = false;

    @Builder.Default
    @Column(nullable = false)
    private Integer hits = 0;

    @CreationTimestamp
    private Date isCreated;

    public PostList(String title, String writer, String writerName,Boolean is_notice,Long postId) {//생성자
        this.title = title;
        this.writer = writer;
        this.is_notice = is_notice;
        this.writerName = writerName;
        this.postId = postId;
    }

    public void update(String title, boolean is_notice) {
        this.title = title;
        this.is_notice = is_notice;
    }
    public void update_hit(Integer hits) {
        System.out.println(this.hits);
        this.hits = hits+1;
        System.out.println(this.hits);
    }
    public void postId(Long postId) {
        this.postId = postId;
    }
}
