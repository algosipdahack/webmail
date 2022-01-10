package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.IMAPMailSystem;
import kr.co.ggabi.springboot.domain.mail.ReceivedWebMail;
import kr.co.ggabi.springboot.domain.mail.WebMail;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.dto.AttachmentResponseDto;
import kr.co.ggabi.springboot.dto.MailResponseDto;
import kr.co.ggabi.springboot.dto.MailboxResponseDto;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.repository.MembersRepository;
import kr.co.ggabi.springboot.repository.ReceivedWebMailRepository;
import kr.co.ggabi.springboot.repository.WebMailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImapMailService {

    private final IMAPMailSystem imapMailSystem;
    private final TokenProvider tokenProvider;
    private final MembersRepository membersRepository;
    private final WebMailRepository webMailRepository;
    private final ReceivedWebMailRepository receivedWebMailRepository;

    @Value("${mailServer.host}")
    String host;

    public Map<Integer, MailboxResponseDto> showMailbox(HttpServletRequest httpServletRequest, String mailBox) throws Exception {
        try {
            imapMailSystem.logout(1);
        } catch (IllegalStateException e){
            System.out.println("before login set");
        } catch (NullPointerException e){
            System.out.println("before login set");
        }
        long id = imapMailSystem.login(host, tokenProvider.resolveToken(httpServletRequest), mailBox);
        Map<Integer, MailboxResponseDto> res = imapMailSystem.getEmailSubjects(id, mailBox);

        // 웹메일 저장
        saveWebMail(res, httpServletRequest, mailBox);

        try {
            imapMailSystem.logout(id);
        } catch (IllegalStateException e){
            System.out.println("end login");
        } catch (NullPointerException e){
            System.out.println("before login set");
        }

        return res;
    }

    public MailResponseDto showMailDetails(HttpServletRequest httpServletRequest, int idx, String mailBox, boolean seen) throws Exception {
        try {
            imapMailSystem.logout(1);
        } catch (IllegalStateException e){
            System.out.println("before login set");
        } catch (NullPointerException e){
            System.out.println("before login set");
        }
        long id = imapMailSystem.login(host, tokenProvider.resolveToken(httpServletRequest), mailBox);
        int msgCount = imapMailSystem.getMessageCount();
        MailResponseDto res = imapMailSystem.getEmailDetails(id, idx, mailBox, seen);
        try {
            imapMailSystem.logout(id);
        } catch (IllegalStateException e){
            System.out.println("end login");
        } catch (NullPointerException e){
            System.out.println("before login set");
        }

        return res;
    }

    public Map<String, String> setMail(HttpServletRequest httpServletRequest, String mailBox, List<Integer> mailIdList, Boolean seen) throws Exception {
        try {
            imapMailSystem.logout(1);
        } catch (IllegalStateException e){
            System.out.println("before login set");
        } catch (NullPointerException e){
            System.out.println("before login set");
        }
        long id = imapMailSystem.login(host, tokenProvider.resolveToken(httpServletRequest), mailBox);
        Map<String, String> res = imapMailSystem.setMail(mailIdList, seen);
        try {
            imapMailSystem.logout(id);
        } catch (IllegalStateException e){
            System.out.println("end login");
        } catch (NullPointerException e){
            System.out.println("before login set");
        }

        return res;
    }

    public Map<String, String> trashMail(HttpServletRequest httpServletRequest, String mailBox, List<Integer> mailIdList) throws Exception {
        try {
            imapMailSystem.logout(1);
        } catch (IllegalStateException e){
            System.out.println("before login set");
        } catch (NullPointerException e){
            System.out.println("before login set");
        }
        long id = imapMailSystem.login(host, tokenProvider.resolveToken(httpServletRequest), mailBox);
        Map<String, String> res = imapMailSystem.trashMail(mailIdList);
        try {
            imapMailSystem.logout(id);
        } catch (IllegalStateException e){
            System.out.println("end login");
        } catch (NullPointerException e){
            System.out.println("before login set");
        }

        return res;
    }

    public void saveWebMail(Map<Integer, MailboxResponseDto> res, HttpServletRequest httpServletRequest, String mailBox) throws Exception {

        Set<Map.Entry<Integer, MailboxResponseDto>> entrySet = res.entrySet();
        Iterator<Map.Entry<Integer, MailboxResponseDto>> resIt = entrySet.iterator();

        while (resIt.hasNext()) {
            Map.Entry<Integer, MailboxResponseDto> resEntry = resIt.next();
            int key = resEntry.getKey();
            MailboxResponseDto value = resEntry.getValue();
            boolean isReceived = false;
            if(mailBox.equals("INBOX")) isReceived = true;
            Optional<WebMail> entity = webMailRepository.findFirstByIsReceivedAndReceiverContainsOrderByIdDesc(isReceived, value.to);
            int mailId = entity.map(webMail -> (webMail.getMailId())).orElse(0);

            if(mailId < key) {
                MailResponseDto dto;
                dto = showMailDetails(httpServletRequest, key, mailBox, false);

                StringBuilder files = new StringBuilder();
                for(Map.Entry<String, AttachmentResponseDto> file : dto.file.entrySet()){
                    files.append(file.getValue().url + " ");
                }

                StringBuilder to = new StringBuilder();
                for(String s : dto.to){
                    to.append(s + " ");
                }

                webMailRepository.save(WebMail.builder()
                        .mailId(key)
                        .receiver(to.toString().trim())
                        .sender(value.from)
                        .subject(value.subject)
                        .date(value.date)
                        .files(files.toString().trim())
                        .isReceived(isReceived)
                        .spamFlag(value.spamFlag)
                        .danger(value.danger)
                        .build());
            }
        }
    }


}
