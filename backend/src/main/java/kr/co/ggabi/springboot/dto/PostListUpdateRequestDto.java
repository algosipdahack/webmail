package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.posts.PostList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostListUpdateRequestDto {
    private String title;
    private Boolean is_notice;
    @Builder
    public PostListUpdateRequestDto(String title,boolean is_notice) {
        this.title = title;
        this.is_notice = is_notice;
    }
}
