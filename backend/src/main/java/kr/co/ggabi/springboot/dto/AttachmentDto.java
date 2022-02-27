package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.board.Board;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AttachmentDto {
    private Long id;
    private String origFilename;
    private String filename;
    private String filePath;
    private long size;

    public AttachmentDto(Attachment entity) {
        this.id = entity.getId();
        this.origFilename = entity.getOrigFilename();
        this.filename = entity.getFilename();
        this.filePath = entity.getFilePath();
        this.size = entity.getSize();
    }

    public Attachment toEntity() {
        Attachment build = Attachment.builder()
                .id(id)
                .origFilename(origFilename)
                .filename(filename)
                .filePath(filePath)
                .size(size)
                .build();
        return build;
    }

    @Builder
    public AttachmentDto(Long id, String origFilename, String filename, String filePath, Long size) {
        this.id = id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
        this.size = size;
    }
}
