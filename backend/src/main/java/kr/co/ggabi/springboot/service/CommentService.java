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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MembersRepository membersRepository;

    @Transactional
    public Comment save(CommentSaveRequestDto requestDto) {
        String writerName = null;
        List<Member> members = membersRepository.findAllDesc();
        for(Member iter: members) {
            if(iter.getUsername().equals(requestDto.getWriter())) {
                writerName = iter.getAddress().getNickname();
                break;
            }
        }
        requestDto.setWriterName(writerName);
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
        Post post = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid));
        List<Comment> list_c = commentRepository.findAllDesc();
        List<Long> post_comment = new ArrayList<>();
        //댓글인 경우
        if(comment.getParentId() == null){
            for (Comment iter_comment : list_c) {
                //대댓글인 경우
                if(iter_comment.getParentId() == null) continue;
                if(iter_comment.getParentId().equals(comment.getId())){
                    //대댓글 삭제
                    post_comment.add(iter_comment.getId());
                    commentRepository.delete(iter_comment);
                }
            }
        }
        post_comment.add(cid);

        commentRepository.delete(comment);

        List <Long> p_comment = post.getCommentId();
        Iterator<Long> it = p_comment.iterator();
        while(it.hasNext()){
            Long tmp = it.next();
            for(Long iter_post:post_comment){
                if(tmp.equals(iter_post)) {
                    it.remove();
                    break;
                }
            }
        }
        post.update_comment(p_comment);
    }
}
