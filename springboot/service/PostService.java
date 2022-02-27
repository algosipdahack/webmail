package kr.co.ggabi.springboot.service;
import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.board.Board;
import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.domain.posts.PostList;
import kr.co.ggabi.springboot.dto.PostListResponseDto;
import kr.co.ggabi.springboot.dto.PostResponseDto;
import kr.co.ggabi.springboot.dto.PostSaveRequestDto;
import kr.co.ggabi.springboot.dto.PostUpdateRequestDto;
import kr.co.ggabi.springboot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostListRepository postListRepository;
    private final AttachmentRepository attachmentRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

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

    public PostResponseDto findById(Long bid, Long pid) {
        Post post = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid));
        PostResponseDto tmp = new PostResponseDto(post);

        //    private PostList postlist;
        Long postlistid = post.getPostlistId();
        PostList postList = postListRepository.findById(postlistid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+postlistid));
        postListRepository.updateShowCount(postlistid);

        tmp.setPostlist(postList);

        //    private List<Attachment> attachment;
        List<Long> attachmentId = post.getAttachmentId();
        List<Attachment> attachment = new ArrayList<>();
        for(Long iter: attachmentId) {
            attachment.add(attachmentRepository.findById(iter).orElseThrow(()->new IllegalArgumentException("해당 첨부파일이 없습니다. id="+iter)));
        }
        tmp.setAttachment(attachment);

        //    private List<Comment> comment;
        List<Long> commentId = post.getCommentId();
        List<Comment> comment = new ArrayList<>();
        for(Long iter: commentId) {
            comment.add(commentRepository.findById(iter).orElseThrow(()->new IllegalArgumentException("해당 댓글이 없습니다. id="+iter)));
        }
        tmp.setComment(comment);
        return tmp;
    }

    @Transactional(readOnly = true) // 조회기능
    public List<PostResponseDto> findAllDesc() {
        return postRepository.findAllDesc().stream()
                .map(PostResponseDto::new)// == .map(posts->new PostsListResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long bid,Long pid) {
        Post post = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid));
        //board내에서 postlistid지우기
        PostList postlist = postListRepository.findById(post.getPostlistId()).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+post.getPostlistId()));
        Board board = boardRepository.findById(bid).orElseThrow(()->new IllegalArgumentException("해당 게시판이 없습니다. id="+bid));
        List<Long> id = board.getPostlistId();
        Iterator<Long> iter = id.iterator();
        while(iter.hasNext()){
            Long tmp = iter.next();
            if (tmp == postlist.getId()) {
                iter.remove();
                board.update_post(id);
                break;
            }
        }
        //comment도 지우기
        List<Long> comment_id = post.getCommentId();
        for(Long iter_comment:comment_id) {
            commentRepository.delete(commentRepository.findById(iter_comment).orElseThrow(()->new IllegalArgumentException("해당 댓글이 없습니다. id="+iter_comment)));
        }
        //postlist 지우기
        postListRepository.delete(postlist);
        //post지우기
        postRepository.delete(post);
    }
    @Transactional
    public void delete_file(Long pid) {
        List <Attachment> attachments = attachmentRepository.findAllDesc();
        List <Long> attachmentId = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid)).getAttachmentId();

        for (Attachment iter : attachments) {
            for(Long post_iter : attachmentId) {
                if(iter.getId().equals(post_iter)) {
                    attachmentRepository.delete(iter);
                }
            }
        }
    }
    @Transactional
    public Long save_comment(Long pid, Long comment_id) {
        Post post = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid));
        List<Long> old_id = post.getCommentId();
        old_id.add(comment_id);
        post.update_comment(old_id);
        return post.getId();
    }
    public Long delete_comment(Long pid, List<Long> comment_id){
        Post post = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid));
        List<Long> old_id = post.getCommentId();
        Iterator<Long> it = old_id.iterator();
        for(Long iter:comment_id) {
            while(it.hasNext()){
                Long tmp = it.next();
                if(tmp == iter) {
                    it.remove();
                    break;
                }
            }
        }
        post.update_comment(old_id);
        return post.getId();
    }
}
