package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.mail.ReceivedWebMail;
import kr.co.ggabi.springboot.dto.AnalysisRequestDto;
import kr.co.ggabi.springboot.dto.File;
import kr.co.ggabi.springboot.repository.ReceivedWebMailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final ReceivedWebMailRepository receivedWebMailRepository;

    @Transactional
    public Map<String, String> save(AnalysisRequestDto request){

        Map<String, String> res = new HashMap<>();

        try {
            File[] files = request.getFiles();
            if(files.length != 0) {
                for (File file : files) {
                    receivedWebMailRepository.save(ReceivedWebMail.builder()
                            .mailId((long) request.getIdx())
                            .spamFlag(request.getSpamFlag())
                            .file(file.getFilename())
                            .danger(file.getDanger())
                            .username(request.getUsername())
                            .build());
                }
            } else {
                receivedWebMailRepository.save(ReceivedWebMail.builder()
                        .mailId((long) request.getIdx())
                        .spamFlag(request.getSpamFlag())
                        .username(request.getUsername())
                        .build());
            }
            res.put("status", "success");
        } catch (Error e){
            e.printStackTrace();
            res.put("status", "fail");
            res.put("error", e.getMessage());
        }

        return res;
    }
}
