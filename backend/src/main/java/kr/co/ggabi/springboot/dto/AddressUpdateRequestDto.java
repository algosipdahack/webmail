package kr.co.ggabi.springboot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddressUpdateRequestDto {
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String department;
    private String position;
    private String company;

    @Builder
    public AddressUpdateRequestDto( String username, String nickname, String phone,
                                       String email, String department, String position, String company) {
        this.username = username;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.position = position;
        this.company = company;
    }
}
