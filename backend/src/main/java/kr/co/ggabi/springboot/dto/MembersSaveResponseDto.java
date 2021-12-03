package kr.co.ggabi.springboot.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembersSaveResponseDto {
    private String status;
    private String message;
}
