package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.dto.BoardListResponseDto;
import kr.co.ggabi.springboot.dto.MemberResponseDto;
import kr.co.ggabi.springboot.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {
    MembersRepository membersRepository;
    @Transactional(readOnly = true) // 조회기능
    public List<MemberResponseDto>   findAllDesc() {
        return membersRepository.findAllDesc().stream()
                .map(MemberResponseDto::new)// == .map(Board->new BoardListResponseDto(board))
                .collect(Collectors.toList());
    }
}
