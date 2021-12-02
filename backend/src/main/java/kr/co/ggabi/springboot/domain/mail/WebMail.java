package kr.co.ggabi.springboot.domain.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class WebMail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mailId;

    @Column(nullable = false)
    private String receiver;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private Date date;

    @Column
    private String file;

    @Builder.Default
    @Column(nullable = false)
    boolean isReceived = false;

    public WebMail(String mailId, String receiver, String sender,
                   String subject, Date date, String file, boolean isReceived){
        this.mailId = mailId;
        this.receiver = receiver;
        this.sender = sender;
        this.subject = subject;
        this.date = date;
        this.file = file;
        this.isReceived = isReceived;
    }

}
