package kr.co.ggabi.springboot.dto;


import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusDto {

    @NotNull
    private String username;

}
