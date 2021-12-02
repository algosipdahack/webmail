package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.mail.WebMail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class WebMailDto {

    private String mailId;
    private String receiver;
    private String sender;
    private String subject;
    private Date date;
    private String file;
    private boolean isReceived = false;

    @Builder
    public WebMailDto(String mailId, String receiver, String sender,
                      String subject, Date date, String file, boolean isReceived){
        this.mailId = mailId;
        this.receiver = receiver;
        this.sender = sender;
        this.subject = subject;
        this.date = date;
        this.file = file;
        this.isReceived = isReceived;
    }

    public WebMail toEntity(){
        return WebMail.builder()
                .mailId(mailId)
                .receiver(receiver)
                .sender(sender)
                .subject(subject)
                .date(date)
                .file(file)
                .isReceived(isReceived)
                .build();
    }


}
