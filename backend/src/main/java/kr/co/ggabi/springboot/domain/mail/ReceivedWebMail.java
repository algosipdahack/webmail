package kr.co.ggabi.springboot.domain.mail;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ReceivedWebMail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long mailId;

    @Column(nullable = false)
    private String username;

    @Builder.Default
    @Column(nullable = false)
    private int spamFlag = 0;

    @Column
    private String file;

    @Builder.Default
    @Column
    private double danger = -1;

    @Builder.Default
    @Column
    private boolean dangerURL = false;

    public ReceivedWebMail(Long mailId, String username, int spamFlag, String file, float danger, boolean dangerURL){
        this.mailId = mailId;
        this.username = username;
        this.spamFlag = spamFlag;
        this.file = file;
        this.danger = danger;
        this.dangerURL = dangerURL;
    }


}
