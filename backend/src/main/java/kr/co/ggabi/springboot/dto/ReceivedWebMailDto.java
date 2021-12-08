package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.mail.ReceivedWebMail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReceivedWebMailDto {
    private Long mailId;
    private String username;
    private int spamFlag = 0;
    private String file;
    private double danger;

    @Builder ReceivedWebMailDto(Long mailId, String username, int spamFlag, String file, double danger){
        this.mailId = mailId;
        this.username = username;
        this.spamFlag = spamFlag;
        this.file = file;
        this.danger = danger;
    }

    public ReceivedWebMail toEntity(){
        return ReceivedWebMail.builder()
                .mailId(mailId)
                .username(username)
                .spamFlag(spamFlag)
                .file(file)
                .danger(danger)
                .build();
    }

}
