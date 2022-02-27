package kr.co.ggabi.springboot.domain.mail;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ApprovalCheckMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sender;      // 결재 요청자

    @Column(nullable = false)
    private int webMailId;

    @Column
    private String approvalCheckMember;     // 결재 승인 상사

    @Column
    private Boolean acceptApproval;

    public ApprovalCheckMember(String sender, int webMailId, String approvalCheckMember, Boolean acceptApproval){
        this.sender = sender;
        this.webMailId = webMailId;
        this.approvalCheckMember = approvalCheckMember;
        this.acceptApproval = acceptApproval;
    }

}
