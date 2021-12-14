package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.dto.MembersSaveResponseDto;
import kr.co.ggabi.springboot.repository.MembersRepository;
import kr.co.ggabi.springboot.dto.MembersSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@RequiredArgsConstructor
@Service
public class CreateMemberService {
    private final MembersRepository membersRepository;

    @Value("${james.dir}")
    private String dir;
    @Value("${mailServer.domain}")
    String domain;

    @Transactional
    public MembersSaveResponseDto save(MembersSaveRequestDto requestDto) throws IOException {
        String username = requestDto.toEntity().getUsername();
        String password = requestDto.toEntity().getPassword();
        System.out.println(dir + " AddUser " + username + "@" + domain + " " + password.substring(6));
        Process process = Runtime.getRuntime().exec(dir + " AddUser " + username + "@" + domain + " " + password.substring(6));
        MembersSaveResponseDto res = new MembersSaveResponseDto("fail", "error");
        if(membersRepository.findByUsername(username).isPresent()){
            res.setMessage("중복 ID입니다.");
        } else if (membersRepository.findByNickname(requestDto.getNickname()).isPresent()){
            res.setMessage("중복 닉네임입니다.");
        } else if (membersRepository.findByPhone(requestDto.getPhone()).isPresent()){
            res.setMessage("중복 전화번호입니다.");
        } else {
            res.setStatus("success");
            res.setMessage("성공");
            membersRepository.save(requestDto.toEntity()).getId();
        }
        return res;
    }
}
