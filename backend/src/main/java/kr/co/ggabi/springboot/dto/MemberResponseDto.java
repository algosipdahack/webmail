package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.users.Address;
import kr.co.ggabi.springboot.domain.users.Authority;
import kr.co.ggabi.springboot.domain.users.Member;

import javax.persistence.Column;
import java.util.Date;

public class MemberResponseDto {

    public Long id;
    public String nickname;
    public String phone;
    public String email;
    public String department;
    public String position;
    public Date birthday;
    public String username;

    public MemberResponseDto(Member member){
        this.id = member.getAddress().getId();
        this.nickname = member.getAddress().getNickname();
        this.phone = member.getAddress().getPhone();
        this.email = member.getAddress().getEmail();
        this.department = member.getAddress().getDepartment();
        this.position = member.getAddress().getPosition();
        this.birthday = member.getBirthday();
        this.username = member.getUsername();
    }

}
