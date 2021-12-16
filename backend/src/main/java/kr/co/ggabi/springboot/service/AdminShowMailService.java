package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.mail.ReceivedWebMail;
import kr.co.ggabi.springboot.domain.mail.WebMail;
import kr.co.ggabi.springboot.dto.*;
import kr.co.ggabi.springboot.repository.MembersRepository;
import kr.co.ggabi.springboot.repository.ReceivedWebMailRepository;
import kr.co.ggabi.springboot.repository.WebMailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminShowMailService {

    private final WebMailRepository webMailRepository;
    private final ReceivedWebMailRepository receivedWebMailRepository;
    private final MembersRepository membersRepository;

    public Map<Long, MailboxResponseDto> showMailbox(HttpServletRequest httpServletRequest) {
        List<WebMail> list = new ArrayList<>(webMailRepository.findAll());
        Map<Long, MailboxResponseDto> map = new HashMap<>();

        for (WebMail iter : list) {
            MailboxResponseDto box = new MailboxResponseDto();

            box.subject = iter.getSubject();
            box.date = iter.getDate();
            box.spamFlag = iter.getSpamFlag();
            box.danger = iter.getDanger();

            /* 받은 메일 */
            if (iter.isReceived()) {
                box.read = true;
                box.nickname = iter.getReceiver().split("@")[0];
                box.from = iter.getSender();
                box.to = iter.getReceiver();
            }
            /* 보낸 메일*/
            else {
                box.read = false;
                box.nickname = iter.getSender().split("@")[0];
                box.from = iter.getSender();
                box.to = iter.getReceiver();
            }
            map.put(iter.getId(), box);
        }
        return map;
    }


    public Optional<WebMailResponseDto> showMailDetail(HttpServletRequest httpServletRequest, int idx) throws Exception {
        WebMailResponseDto res = new WebMailResponseDto();

        Optional<WebMail> option = webMailRepository.findOneById(Long.valueOf(idx));

        res.mailId = option.get().getMailId();
        res.receiver = option.get().getReceiver();
        res.sender = option.get().getSender();
        res.subject = option.get().getSubject();
        res.date = option.get().getDate();
//        res.files = option.get().getFiles();
        res.spamFlag = option.get().getSpamFlag();
        res.dangerURL = option.get().isDangerURL();

        StringBuilder fileName = new StringBuilder();
        StringBuilder dangerValue = new StringBuilder();

        Optional<List<ReceivedWebMail>> receivedWebMail = receivedWebMailRepository.findAllByUsernameAndMailId(option.get().getReceiver().split("@")[0], res.mailId);

        if(receivedWebMail.isPresent()){
            List<ReceivedWebMail> list = receivedWebMail.get();
            for(ReceivedWebMail mail : list){
                fileName.append(mail.getFile()+ " ");
                dangerValue.append(mail.getDanger() + " ");
            }
        }

        res.files = fileName.toString().trim();
        res.dangers = dangerValue.toString().trim();

        return Optional.of(res);
    }

}
