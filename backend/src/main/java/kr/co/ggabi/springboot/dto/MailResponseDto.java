package kr.co.ggabi.springboot.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MailResponseDto {
    public String subject;
    public String nickname;
    public String from;
    public List<String> to;
    public List<String> CC;
    public Date date;
    public String contentType;
    public String content;
    public Map<String, AttachmentResponseDto> file;
    public int flag;
    public boolean dangerURL = false;
}
