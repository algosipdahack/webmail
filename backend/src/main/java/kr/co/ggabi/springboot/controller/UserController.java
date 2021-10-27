package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.dto.UsersSaveRequestDto;
import kr.co.ggabi.springboot.service.CreateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class UserController {
    private final CreateUserService createUserService;

    @PostMapping("/api/user")
    public Long save(@RequestBody UsersSaveRequestDto requestDto){
        return createUserService.save(requestDto);
    }
}