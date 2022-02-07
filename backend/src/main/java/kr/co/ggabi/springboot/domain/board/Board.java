package kr.co.ggabi.springboot.domain.board;
import kr.co.ggabi.springboot.domain.BaseTimeEntity;
import kr.co.ggabi.springboot.domain.posts.PostList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Board extends BaseTimeEntity {
    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincrement
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column
    private List<PostList> postlist;


    @Builder // 빌더 형태로 만들어줌
    public Board(String title, List<PostList> postlist) {//생성자
        this.title = title;
        this.postlist = postlist;
    }

    public void update(String title) {
        this.title = title;
    }
}
