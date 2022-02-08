package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.domain.posts.PostList;
import kr.co.ggabi.springboot.dto.*;
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
public class PostListService {
    private final PostRepository postRepository;
    private final PostListRepository postListRepository;
    private final BoardRepository boardRepository;
    private final AttachmentRepository attachmentRepository;
    @Transactional
    public PostList save(PostListSaveRequestDto requestDto) {
        return postListRepository.save(requestDto.toEntity());
    }

    @Transactional
    public PostList update(Long bid, Long pid, PostListUpdateRequestDto requestDto) {
        PostList postList = postListRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid));
        postList.update(requestDto.getTitle(),requestDto.getIs_notice());
        return postList;
    }

    public PostListResponseDto findById(Long bid, Long pid) {
        PostList postList = postListRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid));
        return new PostListResponseDto(postList);
    }

    @Transactional(readOnly = true) // 조회기능
    public List<PostListResponseDto> findAllDesc() {
        return postListRepository.findAllDesc().stream()
                .map(PostListResponseDto::new)// == .map(posts->new PostsListResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long bid,Long pid) {
        PostList postList = postListRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시판이 없습니다. id="+pid));
        postListRepository.delete(postList);
    }
}