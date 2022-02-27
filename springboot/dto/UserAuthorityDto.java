package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.users.Authority;
import lombok.Getter;

@Getter
public class UserAuthorityDto {
    int id;
    Authority authority;
}
