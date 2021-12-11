package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final MembersRepository membersRepository;

    public String delete(String username){
        try{
            //Process process = Runtime.getRuntime().exec(dir + " RemoveUser " + username + "@" + domain);
            Member member = membersRepository.findByUsername(username).get();
            membersRepository.delete(member);
            return "success";
        } catch (Exception e){
            return "fail";
        }
    }
}
