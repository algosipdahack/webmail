package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.board.Board;
import kr.co.ggabi.springboot.domain.posts.PostList;
import kr.co.ggabi.springboot.dto.AttachmentDto;
import kr.co.ggabi.springboot.dto.BoardListResponseDto;
import kr.co.ggabi.springboot.dto.BoardResponseDto;
import kr.co.ggabi.springboot.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;

    @Transactional
    public Attachment saveFile(AttachmentDto attachmentDto) {
        return attachmentRepository.save(attachmentDto.toEntity());
    }

    @Transactional
    public AttachmentDto getFile(Long id) {
        Attachment file = attachmentRepository.findById(id).get();

        AttachmentDto attachmentDto = AttachmentDto.builder()
                .id(id)
                .origFilename(file.getOrigFilename())
                .filename(file.getFilename())
                .filePath(file.getFilePath())
                .size(file.getSize())
                .build();
        return attachmentDto;
    }

    @Transactional(readOnly = true) // 조회기능
    public List<AttachmentDto>   findAllDesc() {
        return attachmentRepository.findAllDesc().stream()
                .map(AttachmentDto::new)// == .map(Board->new BoardListResponseDto(board))
                .collect(Collectors.toList());
    }
}
