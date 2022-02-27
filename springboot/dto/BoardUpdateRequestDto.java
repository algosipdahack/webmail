package kr.co.ggabi.springboot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardUpdateRequestDto {
    private String title;

    @Builder
    public BoardUpdateRequestDto(String title) {
        this.title = title;
    }

}
