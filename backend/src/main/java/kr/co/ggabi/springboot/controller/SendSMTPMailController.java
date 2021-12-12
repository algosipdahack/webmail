package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.domain.params.MailParam;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.service.SendSmtpMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileTypeMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class SendSMTPMailController {

    private final SendSmtpMailService smtpMailService;
    private final SaveWebMailController saveMail;
    private final TokenProvider tokenProvider;


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


        /* SaveWebMailController로 보내지는 파라미터 정재시키기 */
        String token = tokenProvider.resolveToken(request);
        String username = tokenProvider.getUsernameFromToken(token);
        String sender = username + "@ggabi.co.kr";

        StringBuilder receivers = new StringBuilder("");
        for(String str : receiver) {
            receivers.append(str + " ");        // N명의 receiver를 가질 경우 테이블을 따로 만들지않고, 스페이스로 구분한다.
        }

        Date nowDate = new Date();

        int attachSize = attachments.size();
        if(attachSize == 0)
        {
            saveMail.saveWebMail(sender, receivers.toString(), subject, nowDate, null);
        }
        else
        {
            StringBuilder files = new StringBuilder("");
            for (MultipartFile file : param.attachments)
                files.append(file.getOriginalFilename() + " ");
            saveMail.saveWebMail(sender, receiver.toString(), subject, nowDate, files.toString());
        }

        return res;
    }
}

