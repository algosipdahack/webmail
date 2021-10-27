package kr.co.ggabi.springboot.domain.users;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false, unique=true)
    private String nickname;

    @Column(nullable=false, unique=true)
    private String phone;

    @Column(nullable=false)
    private Date birthday;

    private String email;

    private String gender;

    private String department;

    private String position;

    @Builder
    public Users(String username, String password, String nickname, String phone,
                 Date birthday, String email, String gender, String department, String position){
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
}
