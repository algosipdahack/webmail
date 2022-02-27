package kr.co.ggabi.springboot.dto;


import kr.co.ggabi.springboot.domain.users.Authority;
import kr.co.ggabi.springboot.domain.users.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class MembersSaveRequestDto {
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private Date birthday;
    private String email;
    private String gender;
    private String department;
    private String position;
    private Authority authority;

    @Builder
    public MembersSaveRequestDto(String username, String password, String nickname, String phone, Date birthday,
                                 String email, String gender, String department, String position, Authority authority){
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.birthday = birthday;
        this.email = email;
        this.gender = gender;
        this.department = department;
        this.position = position;
        this.authority = authority;
    }

    public Member toEntity(){
        return Member.builder()
                .username(username)
                .password("{noop}" + password)
                .nickname(nickname)
                .phone(phone)
                .birthday(birthday)
                .email(email)
                .gender(gender)
                .department(department)
                .position(position)
                .authority(authority)
                .build();
    }
}
