package kr.co.ggabi.springboot.domain.users;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
@DynamicUpdate
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="address")
    private Long id;

    @Column(nullable=false)
    private String nickname;//실명

    @Column(nullable=false, unique=true)
    private String phone;

    private String email;

    private Long parentId;

    private String department;

    private String position;

    private String company;

    @CreationTimestamp
    private Date isCreated;

    @Builder
    public Address(String nickname, String phone,
                   String email, String department, String position,String company,Long parentId){
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.position = position;
        this.company = company;
        this.parentId = parentId;
    }
    public void update(String nickname, String phone,
                       String email, String department, String position, String company){
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.position = position;
        this.company = company;
    }
    public void update_depart(String department) {
        this.department = department;
    }

    public void update_position(String position) {
        this.position = position;
    }
}
