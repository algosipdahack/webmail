package kr.co.ggabi.springboot.domain.mail;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class WebMail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "webMail_id")
    private Long id;

    @Column(nullable = false, name = "User_Id")
    private String userId;

    @Column(nullable = false, name = "Mail_Id")
    private String mailId;

    /* Receiver가 여러명일 경우 스페이스로 구분하여 string 형태로 저장시킴. */
    @Column(nullable = false)
    private String receivers;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private Date date;

    /* Files가 여러개일 경우 스페이스로 구분하여 string 형태로 저장시킴. */
    @Column
    private String files;

    @Column(nullable = false)
    private boolean isReceived;

    public WebMail(String mailId, String receivers, String sender,
                   String subject, Date date, String files, boolean isReceived){
        this.mailId = mailId;
        this.receivers = receivers;
        this.sender = sender;
        this.subject = subject;
        this.date = date;
        this.files = files;
        this.isReceived = isReceived;
    }



}
