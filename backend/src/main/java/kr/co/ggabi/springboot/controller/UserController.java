package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.dto.StatusDto;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final TokenProvider tokenProvider;
    private final UserService userService;


    @GetMapping("/status")
    public Map<String, String> status(HttpServletRequest httpServletRequest, StatusDto statusDto){
        String token = tokenProvider.resolveToken(httpServletRequest);
        Map<String, String> res = new HashMap<>();
        res.put("username", tokenProvider.getUsernameFromToken(token));
        res.put("auth", tokenProvider.getAuthFromToken(token));
        return res;
    }
}
