package kr.co.ggabi.springboot.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsTermDto {
    Long normalCount;
    Long maliciousCount;
}
