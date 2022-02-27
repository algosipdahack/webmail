package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.board.Board;
import kr.co.ggabi.springboot.domain.posts.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class BoardSaveRequestDto {
    private String title;
    @Builder
    public BoardSaveRequestDto(String title) {
        this.title = title;
    }
    public Board toEntity() {
        return Board.builder()
                .title(title)
                .build();
    }
}
