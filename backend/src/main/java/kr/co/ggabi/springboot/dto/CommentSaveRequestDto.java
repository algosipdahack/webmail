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
    private Long parent_id;
    private String content;
    private Long writer_id;
    private String writer;
    private Long post_id;
    private MembersRepository memberRepository;

    @Builder
    public CommentSaveRequestDto(String content, Long parent_id, Long writer_id) {
        this.content = content;
        this.writer_id = writer_id;
        this.parent_id = parent_id;

        String writer = null;
        List<Member> members = memberRepository.findAllDesc();
        for(Member iter: members){
            if(iter.getId().equals(writer_id)) {
                writer = iter.getNickname();
                break;
            }
        }
        this.writer = writer;
    }
    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .writer_id(writer_id)
                .parent_id(parent_id)
                .post_id(post_id)
                .writer(writer)
                .build();
    }
}
