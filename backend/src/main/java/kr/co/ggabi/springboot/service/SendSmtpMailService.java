package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.MailParam;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class SendSmtpMailService {

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


    public Map<String, String> sendMail(MailParam param) {

        Map<String, String> res = new HashMap<>();
        Properties props = new Properties();

        try{
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.host", "ggabi.co.kr");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.debug", "true");
            props.put("mail.smtp.auth", "true");

            Authenticator auth = new SMTPAuthenticator("user@ggabi.co.kr", "1234");
            Session mailSession = Session.getDefaultInstance(props, auth);

            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom("user@ggabi.co.kr");
            message.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(param.receiver)));
            message.setSubject(param.subject);
            message.setText(param.contents);

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
