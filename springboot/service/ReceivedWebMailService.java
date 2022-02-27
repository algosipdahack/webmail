package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.dto.ReceivedWebMailDto;
import kr.co.ggabi.springboot.repository.ReceivedWebMailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ReceivedWebMailService {
    private final ReceivedWebMailRepository receivedWebMailRepository;

    @Transactional
    public Long save(ReceivedWebMailDto receivedWebMailDto) throws IOException{
        return receivedWebMailRepository.save(receivedWebMailDto.toEntity()).getMailId();
    }



}
