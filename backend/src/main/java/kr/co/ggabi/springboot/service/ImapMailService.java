package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.IMAPMailSystem;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
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

    public Map<Integer, Map<String, String>> showMailbox(HttpServletRequest httpServletRequest) throws Exception {

        imapMailSystem.login("ggabi.co.kr", tokenProvider.resolveToken(httpServletRequest));
        Map<Integer, Map<String, String>> res = imapMailSystem.getEmailSubjects();
        imapMailSystem.logout();

        return res;
    }

    public Map<String, String> showMailDetails(HttpServletRequest httpServletRequest, int idx) throws Exception {

        imapMailSystem.login("ggabi.co.kr", tokenProvider.resolveToken(httpServletRequest));
        int msgCount = imapMailSystem.getMessageCount();
        Map<String, String> res = imapMailSystem.getEmailDetails(idx);
        imapMailSystem.logout();

        return res;
    }
}
