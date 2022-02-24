package kr.co.ggabi.springboot.dto;


import kr.co.ggabi.springboot.domain.users.Address;
import kr.co.ggabi.springboot.domain.users.Authority;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.repository.AddressRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

import static kr.co.ggabi.springboot.domain.users.Authority.ROLE_ADMIN;

@Setter
@Getter
@NoArgsConstructor
public class MembersSaveRequestDto {

    private String password;
    private Date birthday;
    private Authority authority;
    private String gender;
    private Address address;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String department;
    private String position;
    private String company;
    @Builder
    public MembersSaveRequestDto(String username, String nickname, String phone,
                                 String email, String department, String position, String company,String password, Date birthday, String gender, Authority authority){
        this.username=username;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.position = position;
        this.company = company;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.authority = ROLE_ADMIN;
    }

    public Member toEntity(){
        return Member.builder()
                .password("{noop}" + password)
                .birthday(birthday)
                .gender(gender)
                .authority(authority)
                .address(address)
                .username(username)
                .build();
    }
}
