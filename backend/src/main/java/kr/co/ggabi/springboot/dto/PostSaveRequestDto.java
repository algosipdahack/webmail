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
    private Long writerId;
    private Long boardId;
    private List<Long> attachmentId;
    private List<Long> commentId;
    private PostListRepository postListRepository;

    @Builder
    public PostSaveRequestDto(String content, Long writerId) {
        this.content = content;
        this.writerId = writerId;
    }
    public Post toEntity() {
        return Post.builder()
                .writerId(writerId)
                .content(content)
                .postlistId(postlistId)
                .boardId(boardId)
                .attachmentId(attachmentId)
                .commentId(commentId)
                .build();
    }

}
