package kr.co.ggabi.springboot.dto;

import java.util.Date;
import java.util.Map;

public class MailResponseDto {
    public String subject;
    public String nickname;
    public String from;
    public Date date;
    public String contentType;
    public String content;
    public Map<String, AttachmentResponseDto> file;

}
