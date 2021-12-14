package kr.co.ggabi.springboot.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String grantType;
    private String accessToken;
    private String accessTokenExpiresIn;
    private String authority;
}
