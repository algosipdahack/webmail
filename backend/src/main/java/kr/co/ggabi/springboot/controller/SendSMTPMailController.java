package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.domain.params.MailParam;
import kr.co.ggabi.springboot.service.SendSmtpMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SendSMTPMailController {

    private final SendSmtpMailService smtpMailService;

    @PostMapping("/api/mail/send")
    public Map<String, String> sendMail(@RequestBody MailParam param) {

        Map<String, String> res = this.smtpMailService.sendMail(param);

        return res;
    }
}

