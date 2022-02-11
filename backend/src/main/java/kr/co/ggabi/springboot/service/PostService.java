package kr.co.ggabi.springboot.service;
import kr.co.ggabi.springboot.domain.attachment.Attachment;
import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.dto.PostListResponseDto;
import kr.co.ggabi.springboot.dto.PostResponseDto;
import kr.co.ggabi.springboot.dto.PostSaveRequestDto;
import kr.co.ggabi.springboot.dto.PostUpdateRequestDto;
import kr.co.ggabi.springboot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostListRepository postListRepository;
    private final AttachmentRepository attachmentRepository;
    private final CommentRepository commentRepository;

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
        PostResponseDto tmp = new PostResponseDto(post);
        //    private PostList postlist;
        Long postlistid = post.getPostlistId();
        tmp.setPostlist(postListRepository.findById(postlistid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+postlistid)));
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
