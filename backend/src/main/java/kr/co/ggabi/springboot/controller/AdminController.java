package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.domain.mail.WebMail;
import kr.co.ggabi.springboot.domain.users.Authority;
import kr.co.ggabi.springboot.dto.*;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.service.AdminService;
import kr.co.ggabi.springboot.service.AdminShowMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    private final AdminShowMailService adminShowMailService;

    @PutMapping
    public Map<Integer, Map<String, String>> setUserAuthority(HttpServletRequest httpServletRequest, @RequestBody List<UserAuthorityDto> dto){
        return adminService.setUserAuthority(dto);
    }

    @GetMapping("/{authority}")
    public List<MemberResponseDto> showUser(HttpServletRequest httpServletRequest, @PathVariable("authority") Authority authority){
        return adminService.getUser(authority);
    }

    @GetMapping("/mail")
    public Map<Long, MailboxResponseDto> showMail(HttpServletRequest httpServletRequest) throws Exception{
        return adminShowMailService.showMailbox(httpServletRequest);
    }

    @GetMapping("/mail/{idx}")
    public Optional<WebMailResponseDto> showMailDetails(HttpServletRequest httpServletRequest, @PathVariable("idx") int idx) throws Exception{
        return adminShowMailService.showMailDetail(httpServletRequest, idx);
    }

}
