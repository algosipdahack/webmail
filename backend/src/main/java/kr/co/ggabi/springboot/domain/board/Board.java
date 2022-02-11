package kr.co.ggabi.springboot.domain.board;
import kr.co.ggabi.springboot.domain.BaseTimeEntity;
import kr.co.ggabi.springboot.domain.posts.PostList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Long> postlistId = new ArrayList<>();


    @Builder // 빌더 형태로 만들어줌
    public Board(String title, List<Long> postlistId) {//생성자
        this.title = title;
        this.postlistId = postlistId;
    }

    public void update(String title) {
        this.title = title;
    }
}
