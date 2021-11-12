package kr.co.ggabi.springboot.domain;

import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class IMAPMailSystem {

    private final TokenProvider tokenProvider;
    private final MembersRepository membersRepository;

    private Session session;
    private Store store;
    private Folder folder;
    // hardcoding protocol and the folder
    // it can be parameterized and enhanced as required
    private String protocol = "imaps";
    private String file = "INBOX";

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

    public Map<Integer, Map<String, String>> getEmailSubjects() throws MessagingException {
        Map<Integer, Map<String, String>> res = new HashMap<>();
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
            Map<String, String> mailSubjectMap = new HashMap<>();
            mailSubjectMap.put("Subject", m.getSubject());
            InternetAddress from = (InternetAddress) m.getFrom()[0];
            mailSubjectMap.put("Nickname", from.getPersonal());
            mailSubjectMap.put("From", from.getAddress());
            mailSubjectMap.put("Date", m.getReceivedDate().toString());
            mailSubjectMap.put("Read", "false");
            res.put(m.getMessageNumber(), mailSubjectMap);
        }
        for (Message m : readMessages) {
            Map<String, String> mailSubjectMap = new HashMap<>();
            mailSubjectMap.put("Subject", m.getSubject());
            InternetAddress from = (InternetAddress) m.getFrom()[0];
            mailSubjectMap.put("Nickname", from.getPersonal());
            mailSubjectMap.put("From", from.getAddress());
            mailSubjectMap.put("Date", m.getReceivedDate().toString());
            mailSubjectMap.put("Read", "true");
            res.put(m.getMessageNumber(), mailSubjectMap);
        }

        return res;
    }

    public Map<String, String> getEmailDetails(int idx) throws MessagingException, IOException {
        Message m = folder.getMessage(idx);
        Map<String, String> res = new HashMap<>();
        res.put("Subject", m.getSubject());
        InternetAddress from = (InternetAddress) m.getFrom()[0];
        res.put("Nickname", from.getPersonal());
        res.put("From", from.getAddress());
        res.put("Date", m.getReceivedDate().toString());
        res.put("Content-Type", m.getContentType());
        Object content = m.getContent();
        InputStream in = m.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        Stream<String> stringStream = new BufferedReader(inputStreamReader).lines();
        res.put("Content", stringStream.collect(Collectors.joining("\n")));
        return res;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain") && (bodyPart.getDisposition().toString() != "attachment")) {;
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }

    public void login(String host, String token) throws Exception {
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
        store.connect(host, username + "@ggabi.co.kr", password);
        folder = store.getFolder("inbox"); //inbox는 받은 메일함을 의미
        folder.open(Folder.READ_WRITE);
        //folder.open(Folder.READ_ONLY); //읽기 전용
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
