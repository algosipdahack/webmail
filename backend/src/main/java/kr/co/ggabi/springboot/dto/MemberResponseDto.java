package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.users.Address;
import kr.co.ggabi.springboot.domain.users.Authority;
import kr.co.ggabi.springboot.domain.users.Member;

import java.util.Date;

public class MemberResponseDto {

    public Long id;
    public Date birthday;
    public Authority authority;
    public Address address;
    public String username;

    public MemberResponseDto(Member member){
        this.id = member.getId();
        this.birthday = member.getBirthday();
        this.authority = member.getAuthority();
        this.address = member.getAddress();
        this.username = member.getUsername();
    }

}
