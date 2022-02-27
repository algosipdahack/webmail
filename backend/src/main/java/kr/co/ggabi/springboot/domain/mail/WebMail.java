package kr.co.ggabi.springboot.domain.mail;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class WebMail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int mailId;

    @Column(nullable = false)
    private String receiver;

    @Column(nullable = false)
    private String sender;

    @Column
    private String subject;

    @Column(nullable = false)
    private Date date;

    @Column
    private String files;

    @Column(nullable = false)
    boolean isReceived;

    @Column(nullable = false)
    int spamFlag;

    @Column
    double danger;

    @Column(nullable = false)
    boolean dangerURL;

    public WebMail(int mailId, String receiver, String sender, String subject,
                   Date date, String files, boolean isReceived, int spamFlag, double danger, boolean dangerURL){
        this.mailId = mailId;
        this.receiver = receiver;
        this.sender = sender;
        this.subject = subject;
        this.date = date;
        this.files = files;
        this.isReceived = isReceived;
        this.spamFlag = spamFlag;
        this.danger = danger;
        this.dangerURL = dangerURL;
    }

}
