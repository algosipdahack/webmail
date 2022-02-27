package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.mail.ApprovalCheckMember;
import kr.co.ggabi.springboot.domain.mail.WebMail;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.dto.*;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.repository.ApprovalCheckMemberRepository;
import kr.co.ggabi.springboot.repository.MembersRepository;
import kr.co.ggabi.springboot.repository.WebMailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApprovalMailService {

    private final TokenProvider tokenProvider;
    private final WebMailRepository webMailRepository;
    private final MembersRepository membersRepository;
    private final ApprovalCheckMemberRepository approvalCheckMemberRepository;

    @Transactional
    public Map<Integer, MailboxResponseDto> showMailBox(HttpServletRequest request, String folder){

        String token = tokenProvider.resolveToken(request);
        String username = tokenProvider.getUsernameFromToken(token);

        Map<Integer, MailboxResponseDto> res = null;
        List<WebMail> tmp = new ArrayList<>();

        // (결재 요청자 입장) wait - 결재 요청 대기 메일함 / reject - 거부된 요청 결재 메일함
        // (결재 진행자 입장) request - 결재 요청 대기 메일함 / end - 결재 완료 메일함
        if("wait".equals(folder))
            res = toMailboxDto(webMailRepository.findBySenderAndHaveToApproval(username + "ggabi.ac.kr", true));
        else if("reject".equals(folder))
            res = toMailboxDto(webMailRepository.findBySenderAndIsAcceptApproval(username+"ggabi.ac.kr", false));
        else if("request".equals(folder)) {
            List<ApprovalCheckMember> db = approvalCheckMemberRepository.findByApprovalCheckMemberAndAcceptApproval(username, null);

            if(!db.isEmpty()) {
                for (ApprovalCheckMember approval : db) {
                    tmp.add(webMailRepository.findBySenderAndMailId(approval.getSender(), approval.getWebMailId()));
                }
                res = toMailboxDto(tmp);
            }
        }
        else if("end".equals(folder)) {
            List<ApprovalCheckMember> db = approvalCheckMemberRepository.findByApprovalCheckMember(username);
            if(!db.isEmpty()) {
                for (ApprovalCheckMember approval : db) {
                    tmp.add(webMailRepository.findBySenderAndMailId(approval.getSender(), approval.getWebMailId()));
                }
                res = toMailboxDto(tmp);
            }
        }

        System.out.println(res);

        return res;
    }

    private Map<Integer, MailboxResponseDto> toMailboxDto(List<WebMail> list){
        Map<Integer, MailboxResponseDto> res = new HashMap<>();

        int i=0;
        for(WebMail webMail : list){
            MailboxResponseDto dto = null;
            dto.subject = webMail.getSubject();
            dto.date = webMail.getDate();
            dto.spamFlag = webMail.getSpamFlag();
            dto.danger = webMail.getDanger();

            if(!webMail.isReceived()){
                dto.read = false;
                dto.nickname = webMail.getSender().split("@")[0];
                dto.from = webMail.getSender();
                dto.to = webMail.getReceiver();
            }
            res.put(i, dto);
        }

        return res;
    }

    public WebMailResponseDto showMailDetails(HttpServletRequest request, String folder, int idx){
        List<WebMail> tmp = new ArrayList<>();
        WebMailResponseDto res = null;

        String token = tokenProvider.resolveToken(request);
        String username = tokenProvider.getUsernameFromToken(token);

        // (결재 요청자 입장) wait - 결재 요청 대기 메일함 / reject - 거부된 요청 결재 메일함
        // (결재 진행자 입장) request - 결재 요청 대기 메일함 / end - 결재 완료 메일함
        if("wait".equals(folder))
            tmp.addAll(webMailRepository.findBySenderAndHaveToApproval(username + "ggabi.ac.kr", true));
        else if("reject".equals(folder))
            tmp.addAll(webMailRepository.findBySenderAndIsAcceptApproval(username+"ggabi.ac.kr", false));
        else if("request".equals(folder)) {
            List<ApprovalCheckMember> db = approvalCheckMemberRepository.findByApprovalCheckMemberAndAcceptApproval(username, null);

            if(!db.isEmpty()) {
                for (ApprovalCheckMember approval : db) {
                    tmp.add(webMailRepository.findBySenderAndMailId(approval.getSender(), approval.getWebMailId()));
                }
            }
        }
        else if("end".equals(folder)) {
            List<ApprovalCheckMember> db = approvalCheckMemberRepository.findByApprovalCheckMember(username);
            if(!db.isEmpty()) {
                for (ApprovalCheckMember approval : db) {
                    tmp.add(webMailRepository.findBySenderAndMailId(approval.getSender(), approval.getWebMailId()));
                }
            }
        }

        res.mailId = tmp.get(idx).getMailId();
        res.receiver = tmp.get(idx).getReceiver();
        res.sender = tmp.get(idx).getSender();
        res.subject = tmp.get(idx).getSubject();
        res.contents = tmp.get(idx).getContent();
        res.date = tmp.get(idx).getDate();
        res.files = tmp.get(idx).getFiles();
        res.isReceived = tmp.get(idx).isReceived();
        res.spamFlag = tmp.get(idx).getSpamFlag();
        res.dangers = Double.toString(tmp.get(idx).getDanger());
        res.dangerURL = tmp.get(idx).isDangerURL();
        res.haveToApproval = tmp.get(idx).isHaveToApproval();
        res.isAcceptApproval = tmp.get(idx).getIsAcceptApproval();

        if(res.haveToApproval){
            ArrayList<ApprovalCheckMember> list = new ArrayList<>();
            list.addAll(approvalCheckMemberRepository.findBySenderAndWebMailId(res.sender, res.mailId));

            list.forEach(approvalCheckMember -> {
                AcceptMember mem = new AcceptMember();
                mem.approvalMember = approvalCheckMember.getApprovalCheckMember();
                mem.doApproval = approvalCheckMember.getAcceptApproval();

                Optional<Member> member = membersRepository.findByNickname(res.sender.split("@")[0]);
                mem.department = member.get().getDepartment();
                mem.position = member.get().getPosition();

                res.acceptMember.add(mem);
            });

        }
        else {
            res.isAcceptApproval = null;
            res.acceptMember = null;
        }

        return res;
    }

    public Map<String, String> changeApprovalMail(HttpServletRequest request, String folder, int idx){
        List<WebMail> tmp = new ArrayList<>();
        Map<String, String> res = new HashMap<>();

        String token = tokenProvider.resolveToken(request);
        String username = tokenProvider.getUsernameFromToken(token);

        if("wait".equals(folder) && request.getParameter("request").equals("cancel")) {
            tmp.addAll(webMailRepository.findBySenderAndHaveToApproval(username + "ggabi.ac.kr", true));

            if(tmp.get(idx).isHaveToApproval()){
                tmp.get(idx).setHaveToApproval(false);
                webMailRepository.save(tmp.get(idx));
                res.put("status", "success");
            }
            else{
                res.put("status", "fail");
            }
        }
        else if("request".equals(folder) && !request.getParameter("request").isEmpty()) {
            List<ApprovalCheckMember> db = approvalCheckMemberRepository.findByApprovalCheckMemberAndAcceptApproval(username, null);

            if(!db.isEmpty()) {
                for (ApprovalCheckMember approval : db) {
                    tmp.add(webMailRepository.findBySenderAndMailId(approval.getSender(), approval.getWebMailId()));
                }
            }

            if(tmp.get(idx).isHaveToApproval()){
                ApprovalCheckMember member = approvalCheckMemberRepository.findByApprovalCheckMemberAndWebMailId(username, tmp.get(idx).getMailId());
                if(member.getAcceptApproval() == null && request.getParameter("request").equals("accept")){
                    member.setAcceptApproval(true);
                    approvalCheckMemberRepository.save(member);
                    res.put("status", "success");
                }
                else if(member.getAcceptApproval() == null && request.getParameter("request").equals("refuse")){
                    member.setAcceptApproval(false);
                    approvalCheckMemberRepository.save(member);
                    res.put("status", "success");
                }
                else{
                    res.put("status", "fail");
                }
            }


            List<ApprovalCheckMember> db1 = approvalCheckMemberRepository.findByApprovalCheckMemberAndAcceptApproval(username, null);

            if(!db.isEmpty()) {
                for (ApprovalCheckMember approval : db1) {
                    if(!approval.getAcceptApproval()) {
                        tmp.get(idx).setIsAcceptApproval(false);
                        webMailRepository.save(tmp.get(idx));
                    }
                }
            }
        }
        else{
            res.put("status", "fail");
        }

        return res;

    }

}
