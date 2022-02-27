package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.mail.ReceivedWebMail;
import kr.co.ggabi.springboot.domain.mail.WebMail;
import kr.co.ggabi.springboot.dto.AnalysisRequestDto;
import kr.co.ggabi.springboot.dto.File;
import kr.co.ggabi.springboot.repository.ReceivedWebMailRepository;
import kr.co.ggabi.springboot.repository.WebMailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final WebMailRepository webMailRepository;
    private final ReceivedWebMailRepository receivedWebMailRepository;

    @Transactional
    public Map<String, String> save(AnalysisRequestDto request){

        Map<String, String> res = new HashMap<>();

        try {
            double dangerMax = -1;

            File[] files = request.getFiles();

            if(files.length != 0) {
                for (File file : files) {

                    if(dangerMax < file.getDanger()) dangerMax = file.getDanger();

                    receivedWebMailRepository.save(ReceivedWebMail.builder()
                            .mailId((long) request.getIdx())
                            .spamFlag(request.getSpamFlag())
                            .file(file.getFilename())
                            .danger(file.getDanger())
                            .username(request.getUsername())
                            .dangerURL(request.isDangerURL())
                            .build());
                }
            } else {
                receivedWebMailRepository.save(ReceivedWebMail.builder()
                        .mailId((long) request.getIdx())
                        .spamFlag(request.getSpamFlag())
                        .username(request.getUsername())
                        .dangerURL(request.isDangerURL())
                        .build());
            }

            Optional<List<WebMail>> optional = webMailRepository.findByReceiverContainsAndMailIdAndIsReceivedTrue(request.getUsername()+"@ggabi.co.kr", request.getIdx());

            if (optional.isPresent() && !optional.get().isEmpty()) {
                WebMail webMail = optional.get().get(0);
                if(request.getSpamFlag() > webMail.getSpamFlag()) webMail.setSpamFlag(request.getSpamFlag());
                if(dangerMax > webMail.getDanger()) webMail.setDanger(dangerMax);
                webMailRepository.save(webMail);
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
