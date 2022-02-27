package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.dto.AnalysisRequestDto;
import kr.co.ggabi.springboot.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RestController
@RequestMapping("/api/danger")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping
    public Map<String, String> setDanger(HttpServletRequest request, @RequestBody AnalysisRequestDto requestDto) {
        Map<String, String> res = analysisService.save(requestDto);
        return res;
    }
}
