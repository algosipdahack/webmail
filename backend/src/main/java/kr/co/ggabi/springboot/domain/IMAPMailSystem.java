package kr.co.ggabi.springboot.domain;

import kr.co.ggabi.springboot.domain.mail.ReceivedWebMail;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.dto.AttachmentResponseDto;
import kr.co.ggabi.springboot.dto.MailResponseDto;
import kr.co.ggabi.springboot.dto.MailboxResponseDto;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.repository.MembersRepository;
import kr.co.ggabi.springboot.repository.ReceivedWebMailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Component
@RequiredArgsConstructor
public class IMAPMailSystem {

    private final TokenProvider tokenProvider;
    private final MembersRepository membersRepository;
    private final ReceivedWebMailRepository receivedWebMailRepository;

    private Session session;
    private Store store;
    private Folder folder;
    // hardcoding protocol and the folder
    // it can be parameterized and enhanced as required
    private String protocol = "imaps";
    @Value("${mailServer.domain}")
    String domain;

    public boolean isLoggedIn() {
        return store.isConnected();
    }

    /**
     * 메일 본문 텍스트 내용을 가져옴
     *
     * @param content
     * @return
     * @throws Exception
     */
    public String getEmailText(Object content) throws Exception {
        System.out.println("####  컨텐츠 타입에 따라서 text body 또는 멀티파트 처리 기능 구현이 필요");
        if (content instanceof Multipart) {
            System.out.println("Multipart 이메일임");
        } else {
            System.out.println(content);
        }
        return null;
    }

    public Map<Integer, MailboxResponseDto> getEmailSubjects() throws MessagingException {
        Map<Integer, MailboxResponseDto> res = new HashMap<>();
        Message[] unreadMessages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        Message[] messages = folder.getMessages();
        Set<Integer> unreadMessagesValSet = new HashSet<>();
        Set<Integer> readMessagesValSet = new HashSet<>();

        for (Message m : unreadMessages) {
            unreadMessagesValSet.add(m.getMessageNumber());
        }

        for (Message m : messages) {
            readMessagesValSet.add(m.getMessageNumber());
        }
        readMessagesValSet.removeAll(unreadMessagesValSet);
        Integer[] readMessagesValArr = new Integer[readMessagesValSet.size()];
        readMessagesValSet.toArray(readMessagesValArr);
        Message[] readMessages = folder.getMessages(
                Arrays.stream(readMessagesValArr).mapToInt(Integer::intValue).toArray());

        Flags seenFlag = new Flags(Flags.Flag.SEEN);
        folder.setFlags(unreadMessages, seenFlag, false);
        for (Message m : unreadMessages) {
            MailboxResponseDto mailboxResponseDto = new MailboxResponseDto();
            mailboxResponseDto.subject = m.getSubject();
            InternetAddress from = (InternetAddress) m.getFrom()[0];
            mailboxResponseDto.nickname = from.getPersonal();
            mailboxResponseDto.from = from.getAddress();
            mailboxResponseDto.date = m.getReceivedDate();
            mailboxResponseDto.read = false;
            InternetAddress recipient = (InternetAddress) m.getAllRecipients()[0];
            mailboxResponseDto.to = recipient.getAddress();
            res.put(m.getMessageNumber(), mailboxResponseDto);
        }
        for (Message m : readMessages) {
            MailboxResponseDto mailboxResponseDto = new MailboxResponseDto();
            mailboxResponseDto.subject = m.getSubject();
            InternetAddress from = (InternetAddress) m.getFrom()[0];
            mailboxResponseDto.nickname = from.getPersonal();
            mailboxResponseDto.from = from.getAddress();
            mailboxResponseDto.date = m.getReceivedDate();
            mailboxResponseDto.read = true;
            InternetAddress recipient = (InternetAddress) m.getAllRecipients()[0];
            mailboxResponseDto.to = recipient.getAddress();
            res.put(m.getMessageNumber(), mailboxResponseDto);
        }

        return res;
    }

    public MailResponseDto getEmailDetails(long uid, int idx, String mailBox) throws MessagingException, IOException {
        Message m = folder.getMessage(idx);
        MailResponseDto res = new MailResponseDto();
        res.subject = m.getSubject();
        InternetAddress from = (InternetAddress) m.getFrom()[0];
        res.nickname = from.getPersonal();
        res.from = from.getAddress();
        res.date = m.getReceivedDate();
        res.contentType = m.getContentType();
        InternetAddress recipient = (InternetAddress) m.getAllRecipients()[0];
        res.to = recipient.getAddress();
        Object content = m.getContent();
        InputStream in = m.getInputStream();
        if (content instanceof MimeMultipart) {
            res.content = getTextFromMimeMultipart((MimeMultipart) content);
            if (res.content.equals("")) {
                res.content = getPlainTextFromMimeMultipart((MimeMultipart) content);
            }
            res.file = downloadAttachments(m, uid, idx, mailBox);
        } else {
            res.content = (String) content;
            res.file = new HashMap<>();
        }
        String username = membersRepository.findById(uid).get().getUsername();
        if (mailBox.equals("INBOX")) {
            int flagMax = 0;
            Optional<List<ReceivedWebMail>> optional = receivedWebMailRepository.findAllByUsernameAndMailId(username, (long) idx);
            if (optional.isPresent()) {
                List<ReceivedWebMail> mailList = optional.get();
                for (ReceivedWebMail mail : mailList) {
                    try {
                        if (mail.getFile() != null) {
                            System.out.println(URLEncoder.encode(mail.getFile(), "UTF-8"));
                            AttachmentResponseDto attachment = res.file.get(URLEncoder.encode(mail.getFile(), "UTF-8"));
                            attachment.isAnalysed = true;
                            attachment.danger = mail.getDanger();
                            res.file.replace(mail.getFile(), attachment);
                        }
                        if (flagMax < mail.getSpamFlag()) flagMax = mail.getSpamFlag();
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
            res.flag = flagMax;
        }
        return res;
    }

    private Map<String, AttachmentResponseDto> downloadAttachments(Message message, long uid, int idx, String mailBox) throws IOException, MessagingException{
        Map<String, AttachmentResponseDto> downloadedAttachments = new HashMap<>();
        Multipart multipart = (Multipart) message.getContent();
        int numberOfParts = multipart.getCount();
        String file;
        for (int partCount = 0; partCount < numberOfParts; partCount++) {
            MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(partCount);
            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                String origin = part.getFileName();
                if(origin.substring(0,2).equals("=?") && origin.substring(origin.length() - 2).equals("?=")) {
                    file = MimeUtility.decodeText(part.getFileName()).replace(' ', '+');
                } else{
                    file = part.getFileName().replace(' ', '+');
                }
                String path = "./downloads" + File.separator + mailBox + File.separator + Long.toString(uid) + File.separator + Integer.toString(idx) + File.separator + file;
                File downloads = new File("./downloads");
                downloads.mkdirs();
                File mailBoxDir = new File("./downloads" + File.separator + mailBox);
                mailBoxDir.mkdirs();
                File uidDir = new File("./downloads" + File.separator + mailBox + File.separator + Long.toString(uid));
                uidDir.mkdirs();
                File idxDir = new File("./downloads" + File.separator + mailBox + File.separator + Long.toString(uid) + File.separator + Integer.toString(idx));
                idxDir.mkdirs();
                part.saveFile(path);
                file = URLEncoder.encode(MimeUtility.decodeText(part.getFileName()).replace(' ', '+'), "UTF-8");
                String link = "/api/mail/download/" + mailBox + "/" + Integer.toString(idx) + "/" + file;
                AttachmentResponseDto attachmentResponseDto = new AttachmentResponseDto();
                attachmentResponseDto.size = part.getSize();
                attachmentResponseDto.url = link;
                downloadedAttachments.put(file, attachmentResponseDto);
            }
        }
        return downloadedAttachments;
    }


    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            System.out.println(bodyPart.getDataHandler().toString());
            System.out.println(bodyPart.getDisposition());
            if (bodyPart.isMimeType("text/html") &&
                    (bodyPart.getDisposition() == null ||
                            !(bodyPart.getDisposition().equals("attachment")))) {;
                result = result + "\n" + bodyPart.getContent();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }
    private String getPlainTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            System.out.println(bodyPart.getDataHandler().toString());
            System.out.println(bodyPart.getDisposition());
            if (bodyPart.isMimeType("text/plain") &&
                    (bodyPart.getDisposition() == null ||
                            !(bodyPart.getDisposition().equals("attachment")))) {;
                result = result + "\n" + bodyPart.getContent();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getPlainTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }

    public long login(String host, String token, String mailbox) throws Exception {
        String username = tokenProvider.getUsernameFromToken(token);
        Member member = membersRepository.findByUsername(username).get();
        String password = member.getPassword().substring(6);
        if (session == null) {
            Properties props = null;
            try {
                props = System.getProperties();
            } catch (SecurityException sex) {
                props = new Properties();
            }
            props.setProperty("mail.imap.ssl.enable", "false");
            session = Session.getInstance(props, null);
        }
        store = session.getStore("imap");
        store.connect(host, username + "@" + domain, password);
        folder = store.getFolder(mailbox); //inbox는 받은 메일함을 의미
        folder.open(Folder.READ_WRITE);
        //folder.open(Folder.READ_ONLY); //읽기 전용
        return member.getId();
    }

    public void logout() throws MessagingException {
        folder.close(false);
        store.close();
        store = null;
        session = null;
    }

    public int getMessageCount() {
        int messageCount = 0;
        try {
            messageCount = folder.getMessageCount();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
        return messageCount;
    }

    /**
     * 이메일 리스트를 가져옴
     *
     * @param onlyNotRead 안읽은 메일 리스트만 가져올지 여부
     * @return
     * @throws MessagingException
     **/
    public Message[] getMessages(boolean onlyNotRead) throws MessagingException {
        if (onlyNotRead) {
            return folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        } else {
            return folder.getMessages();
        }
    }
}
