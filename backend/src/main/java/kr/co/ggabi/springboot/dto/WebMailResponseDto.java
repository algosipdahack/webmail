package kr.co.ggabi.springboot.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class WebMailResponseDto {

    public int mailId;
    public String receiver;
    public String sender;
    public String subject;
    public Date date;
    public String files;
    public boolean isReceived = false;
    public int spamFlag;
    public String dangers;
    public boolean dangerURL;

    @Builder
    public WebMailResponseDto(int mailId, String receiver, String sender, String subject,
                      Date date, String files, boolean isReceived, int spamFlag, String dangers, boolean dangerURL){
        this.mailId = mailId;
        this.receiver = receiver;
        this.sender = sender;
        this.subject = subject;
        this.date = date;
        this.files = files;
        this.isReceived = isReceived;
        this.spamFlag = spamFlag;
        this.dangers = dangers;
        this.dangerURL = dangerURL;
    }

}
