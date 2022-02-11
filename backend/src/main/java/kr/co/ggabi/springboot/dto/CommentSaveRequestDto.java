package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.repository.MembersRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class CommentSaveRequestDto {
    private Long parentId;
    private String content;
    private Long writerId;
    private String writer;
    private Long postId;

    @Builder
    public CommentSaveRequestDto(String content, Long parentId, Long writerId) {
        this.content = content;
        this.writerId = writerId;
        this.parentId = parentId;
    }
    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .writerId(writerId)
                .parentId(parentId)
                .postId(postId)
                .writer(writer)
                .build();
    }
}
