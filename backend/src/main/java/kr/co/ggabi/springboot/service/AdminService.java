package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.users.Authority;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.dto.MemberResponseDto;
import kr.co.ggabi.springboot.dto.UserAuthorityDto;
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

    public Map<Integer, Map<String, String>> setUserAuthority(List<UserAuthorityDto> dto){
        Map<Integer, Map<String, String>> res = new HashMap<>();
        for(UserAuthorityDto userAuthority: dto) {
            Map<String, String> inner = new HashMap<>();
            try {
                Optional<Member> optional = membersRepository.findById((long) userAuthority.getId());
                if (optional.isPresent()) {
                    Member member = optional.get();
                    member.setAuthority(userAuthority.getAuthority());
                    membersRepository.save(member);
                    inner.put("status", "success");
                } else {
                    inner.put("status", "fail");
                    inner.put("error", "No user");
                }
            } catch (Exception e) {
                inner.put("status", "fail");
                inner.put("error", e.getMessage());
            }
            res.put(userAuthority.getId(), inner);
        }
        return res;
    }

}
