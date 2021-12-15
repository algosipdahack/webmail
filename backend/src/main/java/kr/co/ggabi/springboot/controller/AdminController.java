package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.domain.users.Authority;
import kr.co.ggabi.springboot.dto.MemberResponseDto;
import kr.co.ggabi.springboot.dto.UserAuthorityDto;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final TokenProvider tokenProvider;
    private final AdminService adminService;

    @PutMapping
    public Map<Integer, Map<String, String>> setUserAuthority(HttpServletRequest httpServletRequest, @RequestBody List<UserAuthorityDto> dto){
        return adminService.setUserAuthority(dto);
    }

    @GetMapping("/{authority}")
    public List<MemberResponseDto> showUser(HttpServletRequest httpServletRequest, @PathVariable("authority") Authority authority){
        return adminService.getUser(authority);
    }
}
