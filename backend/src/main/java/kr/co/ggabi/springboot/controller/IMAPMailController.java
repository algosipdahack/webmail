package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.domain.params.MailParam;
import kr.co.ggabi.springboot.dto.MailResponseDto;
import kr.co.ggabi.springboot.dto.MailboxResponseDto;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.service.FileDownloadService;
import kr.co.ggabi.springboot.service.ImapMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class IMAPMailController {

    private final ImapMailService imapMailService;
    private final TokenProvider tokenProvider;
    private final FileDownloadService service;

    @GetMapping("/mailbox")
    public Map<Integer, MailboxResponseDto> sendMail(HttpServletRequest httpServletRequest) throws Exception {

        return imapMailService.showMailbox(httpServletRequest);
    }

    @GetMapping("/mailbox/{idx}")
    public MailResponseDto showMail(HttpServletRequest httpServletRequest, @PathVariable("idx") int idx) throws Exception {
        return imapMailService.showMailDetails(httpServletRequest, idx);
    }

    @GetMapping("/download/{idx}/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request, @PathVariable("idx") int idx, @PathVariable("filename") String filename) throws IOException {
        String token = tokenProvider.resolveToken(request);
        String username = tokenProvider.getUsernameFromToken(token);
        System.out.println(token);
        System.out.println(username);
        Resource resource = service.loadFileAsResource(username, idx, filename);
        String contentType = null;
        try {
            contentType = request.getServletContext().
                    getMimeType(resource.getFile().getAbsolutePath());
        } catch (Exception e){
            e.printStackTrace();
        }
        if(contentType == null){
            contentType = "application/octet-stream";
        }
        System.out.println(contentType);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
