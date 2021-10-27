package kr.co.ggabi.springboot.dto;


import kr.co.ggabi.springboot.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class UsersSaveRequestDto {
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private Date birthday;
    private String email;
    private String gender;
    private String department;
    private String position;

    @Builder
    public UsersSaveRequestDto(String username, String password, String nickname, String phone, Date birthday,
                               String email, String gender, String department, String position){
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.birthday = birthday;
        this.email = email;
        this.gender = gender;
        this.department = department;
        this.position = position;
    }

    public Users toEntity(){
        return Users.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .phone(phone)
                .birthday(birthday)
                .email(email)
                .gender(gender)
                .department(department)
                .position(position)
                .build();
    }
}
