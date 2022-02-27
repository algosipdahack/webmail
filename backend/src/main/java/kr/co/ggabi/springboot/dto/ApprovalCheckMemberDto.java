package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.mail.ApprovalCheckMember;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApprovalCheckMemberDto {
    private Long id;
    private String sender;
    private int webMailId;
    private String approvalCheckMember;
    private Boolean acceptApproval;

    public ApprovalCheckMemberDto(ApprovalCheckMember entity){
        this.id = entity.getId();
        this.sender = entity.getSender();
        this.webMailId = entity.getWebMailId();
        this.approvalCheckMember = entity.getApprovalCheckMember();
        this.acceptApproval = entity.getAcceptApproval();
    }

    public ApprovalCheckMember toEntity(){
        ApprovalCheckMember build = ApprovalCheckMember.builder()
                .id(id)
                .sender(sender)
                .webMailId(webMailId)
                .approvalCheckMember(approvalCheckMember)
                .acceptApproval(acceptApproval)
                .build();
        return build;
    }

    @Builder
    public ApprovalCheckMemberDto(Long id, String sender, int webMailId, String approvalCheckMember, Boolean acceptApproval){
        this.id = id;
        this.sender = sender;
        this.webMailId = webMailId;
        this.approvalCheckMember = approvalCheckMember;
        this.acceptApproval = acceptApproval;
    }

}
