package kr.co.ggabi.springboot.domain.users;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
@DynamicUpdate
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private Date birthday;

    @OneToOne(fetch = FetchType.LAZY) // 사용시점에 조회가 됨
    @JoinColumn(name="address_id")
    private Address address;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private String gender;

    @Builder
    public Member(String username,String password, Date birthday, String gender,Authority authority,Address address){
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.authority = Authority.ROLE_ADMIN;
        this.address = address;
    }

    public void update(Address address) {
        this.address = address;
    }
}
