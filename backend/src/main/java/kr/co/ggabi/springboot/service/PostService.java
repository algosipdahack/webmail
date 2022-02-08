package kr.co.ggabi.springboot.service;
import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.dto.PostListResponseDto;
import kr.co.ggabi.springboot.dto.PostResponseDto;
import kr.co.ggabi.springboot.dto.PostSaveRequestDto;
import kr.co.ggabi.springboot.dto.PostUpdateRequestDto;
import kr.co.ggabi.springboot.repository.AttachmentRepository;
import kr.co.ggabi.springboot.repository.BoardRepository;
import kr.co.ggabi.springboot.repository.PostListRepository;
import kr.co.ggabi.springboot.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostListRepository postListRepository;
    private final AttachmentRepository attachmentRepository;
    @Transactional
    public Post save(PostSaveRequestDto requestDto) {
        return postRepository.save(requestDto.toEntity());
    }

    @Transactional
    public Long update(Long bid, Long pid, PostUpdateRequestDto requestDto) {
        Post post = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid));
        post.update(requestDto.getContent(),requestDto.getAttachmentId());
        return post.getId();
    }

    public PostResponseDto findById(Long board_id, Long pid) {
        Post post = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid));
        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true) // 조회기능
    public List<PostResponseDto> findAllDesc() {
        return postRepository.findAllDesc().stream()
                .map(PostResponseDto::new)// == .map(posts->new PostsListResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long bid,Long pid) {
        Post post = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시판이 없습니다. id="+pid));
        postRepository.delete(post);
    }
    @Transactional
    public void delete_file(Long pid) {
        List <Attachment> attachments = attachmentRepository.findAllDesc();
        Post post = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid));
        List <Long> attachmentId = post.getAttachmentId();
        for (Attachment iter : attachments) {
            for(Long post_iter : attachmentId) {
                if(iter.getId().equals(post_iter)) {
                    attachmentRepository.delete(iter);
                }
            }
        }
    }
}
