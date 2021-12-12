package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.mail.WebMail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
public class WebMailDto {

    public String mailId;
    public String receiver;
    public String sender;
    public String subject;
    public Date date;
    public String file;
    public boolean isReceived;

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
                .receivers(receiver)
                .sender(sender)
                .subject(subject)
                .date(date)
                .files(file)
                .isReceived(isReceived)
                .build();
    }


}
