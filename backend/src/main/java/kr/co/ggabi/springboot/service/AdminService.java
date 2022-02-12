package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.board.Board;
import kr.co.ggabi.springboot.dto.BoardSaveRequestDto;
import kr.co.ggabi.springboot.repository.BoardRepository;
import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.repository.CommentRepository;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.domain.posts.PostList;
import kr.co.ggabi.springboot.repository.PostListRepository;
import kr.co.ggabi.springboot.repository.PostRepository;
import kr.co.ggabi.springboot.domain.users.Authority;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.dto.BoardUpdateRequestDto;
import kr.co.ggabi.springboot.dto.MemberResponseDto;
import kr.co.ggabi.springboot.dto.UserAuthorityDto;
import kr.co.ggabi.springboot.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MembersRepository membersRepository;
    private final BoardRepository boardRepository;
    private final PostListRepository postListRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public List<MemberResponseDto> getUser(Authority authority){
        List<Member> memberList = membersRepository.findAllByAuthority(authority);
        List<MemberResponseDto> res = new ArrayList<>();
        for(Member m: memberList){
            res.add(new MemberResponseDto(m));
        }
        return res;
    }

    public Map<Integer, Map<String, String>> setUserAuthority(List<UserAuthorityDto> dto){
        Map<Integer, Map<String, String>> res = new HashMap<>();
        for(UserAuthorityDto userAuthority: dto) {
            Map<String, String> inner = new HashMap<>();
            try {
                Optional<Member> optional = membersRepository.findById((long) userAuthority.getId());
                if (optional.isPresent()) {
                    Member member = optional.get();
                    member.setAuthority(userAuthority.getAuthority());
                    membersRepository.save(member);
                    inner.put("status", "success");
                } else {
                    inner.put("status", "fail");
                    inner.put("error", "No user");
                }
            } catch (Exception e) {
                inner.put("status", "fail");
                inner.put("error", e.getMessage());
            }
            res.put(userAuthority.getId(), inner);
        }
        return res;
    }

    @Transactional
    public Long save(BoardSaveRequestDto requestDto) {
        return boardRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, BoardUpdateRequestDto requestDto) {
        Board board = boardRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시판이 없습니다. id="+id));
        board.update(requestDto.getTitle());
        return board.getId();
    }

    @Transactional
    public void delete_board(Long bid) {
        Board board = boardRepository.findById(bid).orElseThrow(()->new IllegalArgumentException("해당 게시판이 없습니다. id="+bid));
        //게시판 삭제
        boardRepository.delete(board);

        // 게시물 삭제 & 댓글 삭제
        //1. POST
        List<Long> list = board.getPostlistId();
        List<Post> post = postRepository.findAll();
        List<Comment> comment = commentRepository.findAll();
        for (Post iter_p: post){
            //댓글 삭제
            for(Comment iter_c:comment) {
                if (iter_c.getPostId().equals(iter_p.getId())) {
                    commentRepository.delete(iter_c);
                }
            }
            for(Long iter:list){
                if(iter_p.getPostlistId().equals(iter)) {
                    //게시물 삭제
                    postRepository.delete(iter_p);
                }
            }
        }
        //2. POSTList
        for (Long id : list) {
            PostList iter = postListRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+id));
            postListRepository.delete(iter);
        }
    }

    //게시물 삭제
    @Transactional
    public void delete_post(Long bid, Long pid) {
        Post post = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid));
        List<PostList> list = postListRepository.findAll();
        for (PostList iter : list) {
            if(iter.getId().equals(post.getPostlistId())) {
                //postlist 삭제
                postListRepository.delete(iter);
            }
        }
        postRepository.delete(post);
    }
}
