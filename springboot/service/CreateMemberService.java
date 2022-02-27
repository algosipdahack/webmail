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

        if("사장".equals(requestDto.getPosition())) requestDto.toEntity().setPosition("1 사장");
        else if("부장".equals(requestDto.getPosition())) requestDto.toEntity().setPosition("2 부장");
        else if("차장".equals(requestDto.getPosition())) requestDto.toEntity().setPosition("3 차장 ");
        else if("과장".equals(requestDto.getPosition())) requestDto.toEntity().setPosition("4 과장");
        else if("대리".equals(requestDto.getPosition())) requestDto.toEntity().setPosition("5 대리");
        else requestDto.toEntity().setPosition("6 사원");

        if("보안".equals(requestDto.getDepartment())) requestDto.toEntity().setDepartment("1 보안");
        else if(("회계".equals(requestDto.getDepartment()))) requestDto.toEntity().setDepartment("2 회계");
        else if(("개발".equals(requestDto.getDepartment()))) requestDto.toEntity().setDepartment("3 개발");
        else if(("영업".equals(requestDto.getDepartment()))) requestDto.toEntity().setDepartment("4 영업");
        else if(("인사".equals(requestDto.getDepartment()))) requestDto.toEntity().setDepartment("5 인사");
        else if(("재무".equals(requestDto.getDepartment()))) requestDto.toEntity().setDepartment("6 재무");
        else requestDto.toEntity().setDepartment("7 기타");

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
