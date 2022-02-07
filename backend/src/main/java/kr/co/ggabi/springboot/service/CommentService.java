package kr.co.ggabi.springboot.service;
import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.dto.CommentListResponseDto;
import kr.co.ggabi.springboot.dto.CommentResponseDto;
import kr.co.ggabi.springboot.dto.CommentSaveRequestDto;
import kr.co.ggabi.springboot.dto.CommentUpdateRequestDto;
import kr.co.ggabi.springboot.repository.BoardRepository;
import kr.co.ggabi.springboot.repository.CommentRepository;
import kr.co.ggabi.springboot.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Comment save(CommentSaveRequestDto requestDto) {
        return commentRepository.save(requestDto.toEntity());
    }

    @Transactional
    public List<Comment> update(Long bid, Long pid, Long cid, CommentUpdateRequestDto requestDto) {
        Post post = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid));
        List<Comment> list_c = post.getComment();
        for (Comment iter : list_c) {
            if(iter.getId().equals(cid)) {
                iter.update(requestDto.getContent());
            }
        }
        post.update_comment(list_c); //post 내에서도 update comment가 필요함
        return list_c;
    }

    public CommentResponseDto findById(Long bid, Long pid, Long id) {
        Post post = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시판이 없습니다. id="+pid));
        List<Comment> list_c = post.getComment();
        for (Comment iter : list_c) {
            if(iter.getId().equals(id)) {
                return new CommentResponseDto(iter);
            }
        }
        return null;
    }

    @Transactional(readOnly = true) // 조회기능
    public List<CommentListResponseDto> findAllDesc() {
        return commentRepository.findAllDesc().stream()
                .map(CommentListResponseDto::new)// == .map(posts->new PostsListResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long bid, Long pid, Long cid) {
        Post post = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid));
        List<Comment> list_c = post.getComment();
        for (Comment iter : list_c) {
            if(iter.getId().equals(cid)) {
                //대댓글이 있는 경우
                if(iter.getParent_id() == null){
                    for (Comment iter_comment : list_c) {
                        //대댓글인 경우
                        if(iter_comment.getParent_id().equals(iter.getId())){
                            //대댓글 삭제
                            commentRepository.delete(iter_comment);
                        }
                    }
                }
                commentRepository.delete(iter);
                break;
            }
        }
    }
}
