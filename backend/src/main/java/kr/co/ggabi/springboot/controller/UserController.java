package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.dto.StatusDto;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.service.AdminService;
import kr.co.ggabi.springboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/user")
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
    //for fixed address
    //체크박스 부서이동
    @PutMapping("/department")
    public void update_depart(@RequestParam("mid") List<String> mid, @RequestParam("department") String department) {
        for (String id : mid) {
            Long lid = Long.parseLong(id);
            adminService.update_depart(lid, department);
        }
    }

    //체크박스 직책이동
    @PutMapping("/position")
    public void update_position(@RequestParam("mid") List<String> mid,@RequestParam("position") String position) {
        for (String id : mid) {
            Long lid = Long.parseLong(id);
            adminService.update_position(lid, position);
        }
    }
}
