package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.repository.MembersRepository;
import kr.co.ggabi.springboot.dto.MembersSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class CreateMemberService {
    private final MembersRepository membersRepository;

    @Value("${james.dir}")
    private String dir;

    @Transactional
    public Long save(MembersSaveRequestDto requestDto){
        String username = requestDto.toEntity().getUsername();
        String password = requestDto.toEntity().getPassword();
        System.out.println(dir + " AddUser " + username + " " + password.substring(6));
        //Process process = Runtime.getRuntime().exec(dir + " AddUser " + username + " " + password.substring(6));
        return membersRepository.save(requestDto.toEntity()).getId();
    }
}
