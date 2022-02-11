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
    private Long writerId;
    private Boolean is_notice;
    private int hits;

    @Builder
    public PostListSaveRequestDto(Long writerId, String title, boolean is_notice) {
        this.title = title;
        this.is_notice = is_notice;
        this.writerId = writerId;
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
