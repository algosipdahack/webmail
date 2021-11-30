package kr.co.ggabi.springboot.domain.params;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class MailParam {
    public String subject;
    public List<String> receiver;
    public List<String> CC;
    public List<String> BCC;
    public String contents;
    public List<MultipartFile> attachments;
}
