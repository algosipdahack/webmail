package kr.co.ggabi.springboot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddressUpdateRequestDto {
    private String nickname;
    private String phone;
    private String email;
    private String department;
    private String position;
    private String company;
    private Long id;
    private String writer;

    @Builder
    public AddressUpdateRequestDto(String writer, Long id,String nickname, String phone,
                                   String email, String department, String position, String company) {
        this.id = id;
        this.writer = writer;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.position = position;
        this.company = company;
    }
}
