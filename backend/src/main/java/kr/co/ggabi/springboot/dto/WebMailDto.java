package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.mail.WebMail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class WebMailDto {

    public int mailId;
    public String receiver;
    public String sender;
    public String subject;
    public Date date;
    public String files;
    public boolean isReceived = false;
    public int spamFlag;
    public double danger;
    public boolean dangerURL;

    @Builder
    public WebMailDto(int mailId, String receiver, String sender, String subject,
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

    public WebMail toEntity(){
        return WebMail.builder()
                .mailId(mailId)
                .receiver(receiver)
                .sender(sender)
                .subject(subject)
                .date(date)
                .files(files)
                .isReceived(isReceived)
                .spamFlag(spamFlag)
                .danger(danger)
                .dangerURL(dangerURL)
                .build();
    }


}
