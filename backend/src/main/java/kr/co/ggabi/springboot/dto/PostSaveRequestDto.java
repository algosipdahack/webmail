package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.domain.mail.ReceivedWebMail;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.domain.posts.PostList;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.repository.MembersRepository;
import kr.co.ggabi.springboot.repository.PostListRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PostSaveRequestDto {
    private Long postlistId;
    private String content;
    private Long boardId;
    private List<Long> attachmentId;

    @Builder
    public PostSaveRequestDto(String content) {
        this.content = content;
    }
    public Post toEntity() {
        return Post.builder()
                .content(content)
                .postlistId(postlistId)
                .boardId(boardId)
                .attachmentId(attachmentId)
                .build();
    }

}
