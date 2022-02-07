package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.posts.PostList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
@Getter
@Setter
@NoArgsConstructor
public class PostUpdateRequestDto {
    private PostList list;
    private List<Attachment> attachment;
    private String content;
    @Builder
    public PostUpdateRequestDto(String title, String content, boolean is_notice) {
        this.list.update(title,is_notice);
        this.content = content;
    }
}
