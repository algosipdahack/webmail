package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.users.Authority;
import kr.co.ggabi.springboot.domain.users.Member;

import java.util.Date;

public class MemberResponseDto {
    public Long id;
    public String username;
    public String nickname;
    public String phone;
    public Date birthday;
    public String department;
    public String position;
    public Date isCreated;
    public Authority authority;

    public MemberResponseDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.department = member.getDepartment();
        this.position = member.getPosition();
        this.birthday = member.getBirthday();
        this.phone = member.getPhone();
        this.isCreated = member.getIsCreated();
        this.authority = member.getAuthority();
    }

}
