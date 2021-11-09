package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.domain.params.MailParam;
import kr.co.ggabi.springboot.service.SendSmtpMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class SendSMTPMailController {

    @Autowired
    private SendSmtpMailService smtpMailService;

    @PostMapping("/api/mail/send")
    public Map<String, String> sendMail(HttpServletRequest request, @RequestBody MailParam param) {

        Map<String, String> res = this.smtpMailService.sendMail(request, param);

        return res;
    }
}

