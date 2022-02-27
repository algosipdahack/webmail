package kr.co.ggabi.springboot.repository;

import kr.co.ggabi.springboot.domain.mail.ApprovalCheckMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ApprovalCheckMemberRepository extends JpaRepository<ApprovalCheckMember, Long> {

    List<ApprovalCheckMember> findByApprovalCheckMemberAndAcceptApproval(String approvalCheckMember, Boolean acceptApproval);
    List<ApprovalCheckMember> findByApprovalCheckMember(String approvalCheckMember);

    List<ApprovalCheckMember> findBySenderAndWebMailId(String sender, int webMailId);
    ApprovalCheckMember findByApprovalCheckMemberAndWebMailId(String approvalCheckMember, int webMailId);

}