package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.domain.params.MailParam;
import kr.co.ggabi.springboot.service.ImapMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class IMAPMailController {

    private final ImapMailService imapMailService;

    @GetMapping("/mailbox")
    public Map<Integer, Map<String, String>> sendMail(HttpServletRequest httpServletRequest) throws Exception {

        return imapMailService.showMailbox(httpServletRequest);
    }

    @GetMapping("/mailbox/{idx}")
    public Map<String, String> showMail(HttpServletRequest httpServletRequest, @PathVariable("idx") int idx) throws Exception {
        return imapMailService.showMailDetails(httpServletRequest, idx);
    }
}
