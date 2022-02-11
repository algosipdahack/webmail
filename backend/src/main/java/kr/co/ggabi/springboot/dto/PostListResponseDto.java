package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.board.Board;
import kr.co.ggabi.springboot.domain.posts.PostList;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

//목록 출력
@Getter
@Setter
public class PostListResponseDto {
    private Long id;
    private String title;
    private String writer;
    private Long writerId;
    private int hits;
    private Boolean is_notice;
    private Board board;
    public Date isCreated;

    public PostListResponseDto(PostList entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.writer = entity.getWriter();
        this.hits = entity.getHits();
        this.writerId = entity.getWriterId();
        this.is_notice = entity.getIs_notice();
        this.isCreated = entity.getIsCreated();
    }
}
