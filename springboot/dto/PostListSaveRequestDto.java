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
    private String writerName;
    private Boolean is_notice;
    private Long postId;
    @Builder
    public PostListSaveRequestDto(String writer, String title, boolean is_notice) {
        this.title = title;
        this.is_notice = is_notice;
        this.writer = writer;
    }
    public PostList toEntity() {
        return PostList.builder()
                .title(title)
                .writer(writer)
                .writerName(writerName)
                .is_notice(is_notice)
                .build();
    }
}
