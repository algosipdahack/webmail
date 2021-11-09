package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.params.MailParam;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class SendSmtpMailService {

    private final TokenProvider tokenProvider;

    public class SMTPAuthenticator extends Authenticator {
        protected String username = "";
        protected String password = "";
        public SMTPAuthenticator(String user, String pwd) {
            username = user;
            password = pwd;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }


    public Map<String, String> sendMail(HttpServletRequest request, MailParam param) {

        Map<String, String> res = new HashMap<>();
        Properties props = new Properties();
        String token = tokenProvider.resolveToken(request);
        String username = tokenProvider.getUsernameFromToken(token);

        try{
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.host", "ggabi.co.kr");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.debug", "true");
            props.put("mail.smtp.auth", "true");

            Authenticator auth = new SMTPAuthenticator(username + "@ggabi.co.kr", "");
            Session mailSession = Session.getDefaultInstance(props, auth);

            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(username + "@ggabi.co.kr");
            message.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(param.receiver)));
            message.setSubject(param.subject);
            message.setContent(param.contents, "text/html;charset=utf-8");

            Transport.send(message);
            res.put("status", "success");

        } catch (AddressException e) {
            e.printStackTrace();
            res.put("status", "fail");

        } catch (MessagingException e) {
            e.printStackTrace();
            res.put("status", "fail");

        }

        return res;
    }
}
