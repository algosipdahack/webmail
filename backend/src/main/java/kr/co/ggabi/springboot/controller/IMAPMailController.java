package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.dto.MailResponseDto;
import kr.co.ggabi.springboot.dto.MailboxResponseDto;
import kr.co.ggabi.springboot.dto.SetMailDto;
import kr.co.ggabi.springboot.dto.TrashMailDto;
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

    @GetMapping("/{mailBox}")
    public Map<Integer, MailboxResponseDto> sendMail(HttpServletRequest httpServletRequest, @PathVariable("mailBox") String mailBox) throws Exception {
        return imapMailService.showMailbox(httpServletRequest, mailBox);
    }

    @GetMapping("/{mailBox}/{idx}")
    public MailResponseDto showMail(HttpServletRequest httpServletRequest, @PathVariable("mailBox") String mailBox, @PathVariable("idx") int idx) throws Exception {
        return imapMailService.showMailDetails(httpServletRequest, idx, mailBox, true);
    }

    @GetMapping("/download/{mailBox}/{idx}/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request, @PathVariable("mailBox") String mailBox, @PathVariable("idx") int idx, @PathVariable("filename") String filename) throws IOException {
        String token = tokenProvider.resolveToken(request);
        String username = tokenProvider.getUsernameFromToken(token);
        System.out.println(token);
        System.out.println(username);
        Resource resource = service.loadFileAsResource(username, idx, filename, mailBox);
        String contentType = null;
        try {
            contentType = request.getServletContext().
                    getMimeType(resource.getFile().getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        System.out.println(contentType);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PutMapping("/{mailBox}")
    public Map<String, String> setMail(HttpServletRequest httpServletRequest,
                                       @PathVariable("mailBox") String mailBox,
                                       @RequestBody SetMailDto body) throws Exception {
        return imapMailService.setMail(httpServletRequest, mailBox, body.mailIdList, body.seen);
    }

    @DeleteMapping("/{mailBox}")
    public Map<String, String> trashMail(HttpServletRequest httpServletRequest,
                                       @PathVariable("mailBox") String mailBox,
                                       @RequestBody TrashMailDto body) throws Exception {
        return imapMailService.trashMail(httpServletRequest, mailBox, body.mailIdList);
    }

}
