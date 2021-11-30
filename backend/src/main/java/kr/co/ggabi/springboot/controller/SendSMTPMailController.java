package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.domain.params.MailParam;
import kr.co.ggabi.springboot.service.SendSmtpMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SendSMTPMailController {

    private final SendSmtpMailService smtpMailService;

    @PostMapping("/api/mail/send")
    public Map<String, String> sendMail(HttpServletRequest request,
                                        @RequestParam("receiver") List<String> receiver,
                                        @RequestParam("CC") List<String> CC,
                                        @RequestParam("BCC") List<String> BCC,
                                        @RequestParam("contents") String contents,
                                        @RequestParam("subject") String subject,
                                        @RequestParam("attachments") List<MultipartFile> attachments) {

        MailParam param = new MailParam();
        param.receiver = receiver;
        param.contents = contents;
        param.CC = CC;
        param.BCC = BCC;
        param.subject = subject;
        param.attachments = attachments;
        Map<String, String> res = this.smtpMailService.sendMail(request, param);

        return res;
    }
}

