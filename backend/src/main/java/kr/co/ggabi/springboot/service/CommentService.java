package kr.co.ggabi.springboot.service;
import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.dto.CommentListResponseDto;
import kr.co.ggabi.springboot.dto.CommentResponseDto;
import kr.co.ggabi.springboot.dto.CommentSaveRequestDto;
import kr.co.ggabi.springboot.dto.CommentUpdateRequestDto;
import kr.co.ggabi.springboot.repository.BoardRepository;
import kr.co.ggabi.springboot.repository.CommentRepository;
import kr.co.ggabi.springboot.repository.MembersRepository;
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
    private final MembersRepository membersRepository;

    @Transactional
    public Comment save(CommentSaveRequestDto requestDto) {
        String writer = null;
        List<Member> members = membersRepository.findAllDesc();
        for(Member iter: members){
            if(iter.getId().equals(requestDto.getWriterId())) {
                writer = iter.getNickname();
                break;
            }
        }
        requestDto.setWriter(writer);
        return commentRepository.save(requestDto.toEntity());
    }

    @Transactional
    public Long update(Long bid, Long pid, Long cid, CommentUpdateRequestDto requestDto) {
        Comment comment = commentRepository.findById(cid).orElseThrow(()->new IllegalArgumentException("해당 댓글이 없습니다. id="+cid));
        comment.update(requestDto.getContent());
        return comment.getId();
    }

    public CommentResponseDto findById(Long bid, Long pid, Long cid) {
        Comment comment = commentRepository.findById(cid).orElseThrow(()->new IllegalArgumentException("해당 댓글이 없습니다. id="+cid));
        return new CommentResponseDto(comment);
    }

    @Transactional(readOnly = true) // 조회기능
    public List<CommentListResponseDto> findAllDesc() {
        return commentRepository.findAllDesc().stream()
                .map(CommentListResponseDto::new)// == .map(posts->new PostsListResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long bid, Long pid, Long cid) {
        Comment comment = commentRepository.findById(cid).orElseThrow(()->new IllegalArgumentException("해당 댓글이 없습니다. id="+cid));
        List<Comment> list_c = commentRepository.findAllDesc();
        //댓글인 경우
        if(comment.getParentId() == null){
            for (Comment iter_comment : list_c) {
                //대댓글인 경우
                if(iter_comment.getParentId().equals(comment.getId())){
                    //대댓글 삭제
                    commentRepository.delete(iter_comment);
                }
            }
        }
        commentRepository.delete(comment);
    }
}
