package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.users.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AddressSaveRequestDto {
    private String nickname;
    private String phone;
    private String email;
    private Long parentId;
    private String department;
    private String position;
    private String company;
    private String writer;
    @Builder
    public AddressSaveRequestDto(String writer, String nickname, String phone,
                                 String email, String department, String position, String company){
        this.writer = writer;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.position = position;
        this.company = company;
    }

    public Address toEntity(){
        return Address.builder()
                .nickname(nickname)
                .phone(phone)
                .email(email)
                .department(department)
                .position(position)
                .company(company)
                .parentId(parentId)
                .build();
    }
}
