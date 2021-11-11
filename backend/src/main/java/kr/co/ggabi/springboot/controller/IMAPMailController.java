package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.domain.params.MailParam;
import kr.co.ggabi.springboot.service.ImapMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class IMAPMailController {

    private final ImapMailService imapMailService;

    @GetMapping("/api/mail/mailbox")
    public Map<Integer, Map<String, String>> sendMail(HttpServletRequest httpServletRequest) throws Exception {

        return imapMailService.showMailbox(httpServletRequest);

    }
}
