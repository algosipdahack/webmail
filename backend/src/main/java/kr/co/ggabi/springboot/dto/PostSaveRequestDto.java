package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.domain.posts.PostList;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.repository.MembersRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PostSaveRequestDto {
    private PostList list;
    private String content;
    private Long writer_id;
    private Long board_id;
    private List<Attachment> attachment;
    private List<Comment> comment;
    private MembersRepository membersRepository;
    @Builder
    public PostSaveRequestDto(String title, String content, boolean is_notice, Long writer_id) {
        this.content = content;
        this.writer_id = writer_id;
        String writer = null;
        List<Member> members = membersRepository.findAllDesc();
        for(Member iter: members){
            if(iter.getId().equals(writer_id)) {
                writer = iter.getNickname();
                break;
            }
        }
        this.list.save(title,is_notice,writer);
    }
    public Post toEntity() {
        return Post.builder()
                .writer_id(writer_id)
                .content(content)
                .list(list)
                .board_id(board_id)
                .attachment(attachment)
                .comment(comment)
                .build();
    }

}
