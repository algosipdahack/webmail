package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.board.Board;
import kr.co.ggabi.springboot.domain.posts.PostList;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private List<PostList> post;

    public BoardResponseDto(Board entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.post = entity.getPostlist();
    }
}
