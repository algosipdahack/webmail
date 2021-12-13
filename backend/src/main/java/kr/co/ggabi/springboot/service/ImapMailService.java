package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.IMAPMailSystem;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.dto.MailResponseDto;
import kr.co.ggabi.springboot.dto.MailboxResponseDto;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.repository.MembersRepository;
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

    @Value("${mailServer.host}")
    String host;

    public Map<Integer, MailboxResponseDto> showMailbox(HttpServletRequest httpServletRequest, String mailBox) throws Exception {

        long id = imapMailSystem.login(host, tokenProvider.resolveToken(httpServletRequest), mailBox);
        Map<Integer, MailboxResponseDto> res = imapMailSystem.getEmailSubjects(id, mailBox);
        imapMailSystem.logout(id);

        return res;
    }

    public MailResponseDto showMailDetails(HttpServletRequest httpServletRequest, int idx, String mailBox) throws Exception {

        long id = imapMailSystem.login(host, tokenProvider.resolveToken(httpServletRequest), mailBox);
        int msgCount = imapMailSystem.getMessageCount();
        MailResponseDto res = imapMailSystem.getEmailDetails(id, idx, mailBox);
        imapMailSystem.logout(id);

        return res;
    }

    public Map<String, String> setMail(HttpServletRequest httpServletRequest, String mailBox, List<Integer> mailIdList, Boolean seen) throws Exception {

        long id = imapMailSystem.login(host, tokenProvider.resolveToken(httpServletRequest), mailBox);
        Map<String, String> res = imapMailSystem.setMail(mailIdList, seen);
        imapMailSystem.logout(id);

        return res;
    }

    public Map<String, String> trashMail(HttpServletRequest httpServletRequest, String mailBox, List<Integer> mailIdList) throws Exception {

        long id = imapMailSystem.login(host, tokenProvider.resolveToken(httpServletRequest), mailBox);
        Map<String, String> res = imapMailSystem.trashMail(mailIdList);
        imapMailSystem.logout(id);

        return res;
    }
}
