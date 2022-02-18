package kr.co.ggabi.springboot.dto;


import kr.co.ggabi.springboot.domain.users.Address;
import kr.co.ggabi.springboot.domain.users.Authority;
import kr.co.ggabi.springboot.domain.users.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class MembersSaveRequestDto {

    private String password;
    private Date birthday;
    private Authority authority;
    private String gender;
    private Address address;

    @Builder
    public MembersSaveRequestDto(String password, Date birthday, String gender, Authority authority){
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.authority = authority;
    }

    public Member toEntity(){
        return Member.builder()
                .password("{noop}" + password)
                .birthday(birthday)
                .gender(gender)
                .authority(authority)
                .address(address)
                .build();
    }
}
