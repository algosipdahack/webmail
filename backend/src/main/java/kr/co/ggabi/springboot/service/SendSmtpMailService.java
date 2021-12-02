package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.params.MailParam;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileTypeMap;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class SendSmtpMailService {

    private final MembersRepository membersRepository;
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
        Member member = membersRepository.findByUsername(username).get();
        String password = member.getPassword().substring(6);

        try{
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.host", "ggabi.co.kr");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.debug", "true");
            props.put("mail.smtp.auth", "true");

            Authenticator auth = new SMTPAuthenticator(username + "@ggabi.co.kr", password);
            Session mailSession = Session.getDefaultInstance(props, auth);

            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(username + "@ggabi.co.kr");
            for(String s: param.receiver) {
                message.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(s)));
            }
            System.out.println(username);
            System.out.println(password);
            System.out.println(param.subject);
            System.out.println(param.contents);
            message.setSubject(param.subject);
            message.setContent(param.contents, "text/html;charset=utf-8");
            for(String s: param.CC) {
                System.out.println(s);
                message.addRecipients(Message.RecipientType.CC, String.valueOf(new InternetAddress(s)));
            }
            for(String s: param.BCC) {
                message.addRecipients(Message.RecipientType.BCC, String.valueOf(new InternetAddress(s)));
            }
            if(!param.attachments.isEmpty()) {
                BodyPart textPart = new MimeBodyPart();
                textPart.setContent(param.contents, "text/html;charset=utf-8");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(textPart);
                for (MultipartFile file : param.attachments) {
                    BodyPart bodyPart = new MimeBodyPart();
                    String mimeType = FileTypeMap.getDefaultFileTypeMap().getContentType(file.getOriginalFilename());
                    DataSource dataSource = new ByteArrayDataSource(file.getBytes(), mimeType);
                    bodyPart.setDataHandler(new DataHandler(dataSource));
                    bodyPart.setFileName(file.getOriginalFilename());
                    bodyPart.setDisposition(Part.ATTACHMENT);
                    multipart.addBodyPart(bodyPart);
                }
                message.setContent(multipart);
            }
            Transport.send(message);
            res.put("status", "success");

        } catch (AddressException e) {
            e.printStackTrace();
            res.put("status", "fail");

        } catch (MessagingException e) {
            e.printStackTrace();
            res.put("status", "fail");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }
}
