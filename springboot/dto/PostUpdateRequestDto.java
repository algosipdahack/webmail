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
    private List<Long> attachmentId;
    private String content;
    @Builder
    public PostUpdateRequestDto(String content) {
        this.content = content;
    }
}
