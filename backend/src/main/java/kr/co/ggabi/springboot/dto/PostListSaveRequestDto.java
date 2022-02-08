package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.domain.posts.PostList;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.repository.MembersRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PostListSaveRequestDto {
    private String title;
    private String writer;
    private int writerId;
    private Boolean is_notice;
    private int hits;
    private MembersRepository membersRepository;

    @Builder
    public PostListSaveRequestDto(Long writerId, String title, boolean is_notice) {
        this.title = title;
        this.is_notice = is_notice;
        String writer = null;
        List<Member> members = membersRepository.findAllDesc();
        for(Member iter: members){
            if(iter.getId().equals(writerId)) {
                writer = iter.getNickname();
                break;
            }
        }
        this.writer = writer;
    }
    public PostList toEntity() {
        return PostList.builder()
                .title(title)
                .writer(writer)
                .is_notice(is_notice)
                .hits(hits)
                .build();
    }
}
