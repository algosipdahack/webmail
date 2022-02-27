package kr.co.ggabi.springboot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AcceptMember {

    public String approvalMember;
    public String department;
    public String position;
    public boolean doApproval;

    @Builder
    public AcceptMember(String approvalMember, String department, String position, boolean doApproval){
        this.approvalMember = approvalMember;
        this.department = department;
        this.position = position;
        this.doApproval = doApproval;
    }

}
