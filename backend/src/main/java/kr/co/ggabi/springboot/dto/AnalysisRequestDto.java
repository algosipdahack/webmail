package kr.co.ggabi.springboot.dto;

import lombok.Getter;

@Getter
public class AnalysisRequestDto {
    String username;
    int idx;
    int spamFlag;
    File[] files;
    boolean dangerURL;
}
