package kr.co.ggabi.springboot.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
public class WebMailResponseDto {

    public int mailId;
    public String receiver;
    public String sender;
    public String subject;
    public String contents;
    public Date date;
    public String files;
    public boolean isReceived = false;
    public int spamFlag;
    public String dangers;
    public boolean dangerURL;
    public boolean haveToApproval;
    public Boolean isAcceptApproval;
    public List<AcceptMember> acceptMember;

    @Builder
    public WebMailResponseDto(int mailId, String receiver, String sender, String subject, String contents,
                      Date date, String files, boolean isReceived, int spamFlag, String dangers, boolean dangerURL,
                              boolean haveToApproval, Boolean isAcceptApproval, List<AcceptMember> acceptMember){
        this.mailId = mailId;
        this.receiver = receiver;
        this.sender = sender;
        this.subject = subject;
        this.contents = contents;
        this.date = date;
        this.files = files;
        this.isReceived = isReceived;
        this.spamFlag = spamFlag;
        this.dangers = dangers;
        this.dangerURL = dangerURL;
        this.haveToApproval = haveToApproval;
        this.isAcceptApproval = isAcceptApproval;
        this.acceptMember = acceptMember;
    }

}
