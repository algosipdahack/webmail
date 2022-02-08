package kr.co.ggabi.springboot.domain.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column
    private String writer;

    @Builder.Default
    @Column(nullable = false)
    private Boolean is_notice = false;

    @Builder.Default
    @Column(nullable = false)
    private int hits = 0;

    public PostList(String title, String writer,Boolean is_notice) {//생성자
        this.title = title;
        this.writer = writer;
        this.is_notice = is_notice;
    }

    public void update(String title, boolean is_notice) {
        this.title = title;
        this.is_notice = is_notice;
    }
}
