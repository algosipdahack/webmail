package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.board.Board;
import lombok.Getter;

@Getter
public class BoardListResponseDto {
    private Long id;
    private String title;

    public BoardListResponseDto(Board entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
    }
}
