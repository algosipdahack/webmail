package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.dto.WebMailDto;
import kr.co.ggabi.springboot.repository.WebMailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class SaveWebMailService {
    private final WebMailRepository webmailRepository;

    @Transactional
    public Long save(WebMailDto webmaildto) throws IOException{
        /* 체크 용도 */
        String mailId = webmaildto.toEntity().getMailId();
        String receiver = webmaildto.toEntity().getReceiver();
        String sender = webmaildto.toEntity().getSender();
        System.out.println("\n메일 idx : "+ mailId + "\nreceiver : " + receiver + "  sender : "+sender);

        return webmailRepository.save(webmaildto.toEntity()).getId();
    }


}
