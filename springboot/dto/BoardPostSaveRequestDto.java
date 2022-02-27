package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.board.Board;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

public class BoardPostSaveRequestDto {
    private String title;
    private List<Long> list;

    @Builder
    public BoardPostSaveRequestDto(Long id) {
        this.list.add(id);
    }
    public Board toEntity() {
        return Board.builder()
                .title(title)
                .postlistId(list)
                .build();
    }
}
