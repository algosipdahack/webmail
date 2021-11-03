package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.repository.MembersRepository;
import kr.co.ggabi.springboot.dto.MembersSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class CreateMemberService {
    private final MembersRepository membersRepository;

    @Transactional
    public Long save(MembersSaveRequestDto requestDto){
        return membersRepository.save(requestDto.toEntity()).getId();
    }
}
