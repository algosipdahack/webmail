package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.domain.users.Authority;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.dto.MemberResponseDto;
import kr.co.ggabi.springboot.dto.SetUserAuthorityDto;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.repository.MembersRepository;
import kr.co.ggabi.springboot.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final TokenProvider tokenProvider;
    private final AdminService adminService;

    @PutMapping
    public Map<String, String> setUserAuthority(HttpServletRequest httpServletRequest, @RequestBody SetUserAuthorityDto dto){
        return adminService.setUserAuthority(dto.getId(), dto.getAuthority());
    }

    @GetMapping("/{authority}")
    public List<MemberResponseDto> showUser(HttpServletRequest httpServletRequest, @PathVariable("authority") Authority authority){
        return adminService.getUser(authority);
    }
}
