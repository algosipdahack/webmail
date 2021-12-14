package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.users.Authority;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.dto.MemberResponseDto;
import kr.co.ggabi.springboot.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MembersRepository membersRepository;

    public List<MemberResponseDto> getUser(Authority authority){
        List<Member> memberList = membersRepository.findAllByAuthority(authority);
        List<MemberResponseDto> res = new ArrayList<>();
        for(Member m: memberList){
            res.add(new MemberResponseDto(m));
        }
        return res;
    }

    public Map<String, String> setUserAuthority(int id, Authority authority){
        Map<String, String> res = new HashMap<>();
        try {
            Optional<Member> optional = membersRepository.findById((long)id);
            if (optional.isPresent()) {
                Member member = optional.get();
                member.setAuthority(authority);
                membersRepository.save(member);
                res.put("status", "success");
            } else {
                res.put("status", "fail");
                res.put("error", "No user");
            }
        } catch (Exception e){
            res.put("status", "fail");
            res.put("error", e.getMessage());
        }
        return res;
    }

}
