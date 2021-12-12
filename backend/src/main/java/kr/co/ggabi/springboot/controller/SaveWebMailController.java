package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.domain.mail.WebMail;
import kr.co.ggabi.springboot.dto.WebMailDto;
import kr.co.ggabi.springboot.repository.WebMailRepository;
import kr.co.ggabi.springboot.service.SaveWebMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class SaveWebMailController {

    @Autowired
    private final SaveWebMailService saveWebMailService;
    private final WebMailRepository webMailRepository;

    public void saveWebMail(String sender, String receiver, String subject, Date date, String file){

        WebMailDto webMailDto = new WebMailDto();
        Optional<WebMail> entity = webMailRepository.findFirstBySenderOrderByIdDesc(sender);
        int inputMailId;
        inputMailId = entity.map(webMail -> Integer.parseInt(webMail.getMailId()) + 1).orElse(0);

        webMailDto.mailId = Integer.toString(inputMailId);
        webMailDto.receiver = receiver;
        webMailDto.sender = sender;
        webMailDto.subject = subject;
        webMailDto.date = date;
        webMailDto.file = file;
        webMailDto.isReceived = false;

       try {
            this.saveWebMailService.save(webMailDto);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
