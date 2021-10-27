package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.users.UsersRepository;
import kr.co.ggabi.springboot.dto.UsersSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class CreateUserService {
    private final UsersRepository usersRepository;

    @Transactional
    public Long save(UsersSaveRequestDto requestDto){
        return usersRepository.save(requestDto.toEntity()).getId();
    }
}
