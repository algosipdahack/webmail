package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.board.Board;
import kr.co.ggabi.springboot.domain.users.Address;
import kr.co.ggabi.springboot.dto.BoardSaveRequestDto;
import kr.co.ggabi.springboot.repository.*;
import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.domain.posts.PostList;
import kr.co.ggabi.springboot.domain.users.Authority;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.dto.BoardUpdateRequestDto;
import kr.co.ggabi.springboot.dto.MemberResponseDto;
import kr.co.ggabi.springboot.dto.UserAuthorityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AddressRepository addressRepository;
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
    public Long save_board(BoardSaveRequestDto requestDto) {
        return boardRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update_board(Long id, BoardUpdateRequestDto requestDto) {
        Board board = boardRepository.findById(id).orElseThrow(()->new IllegalArgumentException("?????? ???????????? ????????????. id="+id));
        board.update(requestDto.getTitle());
        return board.getId();
    }

    @Transactional
    public void delete_board(Long bid) {
        Board board = boardRepository.findById(bid).orElseThrow(()->new IllegalArgumentException("?????? ???????????? ????????????. id="+bid));
        //????????? ??????
        boardRepository.delete(board);

        // ????????? ?????? & ?????? ??????
        //1. POST
        List<Long> list = board.getPostlistId();
        List<Post> post = postRepository.findAll();
        List<Comment> comment = commentRepository.findAll();
        for (Post iter_p: post){
            //?????? ??????
            for(Comment iter_c:comment) {
                if (iter_c.getPostId().equals(iter_p.getId())) {
                    commentRepository.delete(iter_c);
                }
            }
            for(Long iter:list){
                if(iter_p.getPostlistId().equals(iter)) {
                    //????????? ??????
                    postRepository.delete(iter_p);
                }
            }
        }
        //2. POSTList
        for (Long id : list) {
            PostList iter = postListRepository.findById(id).orElseThrow(()->new IllegalArgumentException("?????? ???????????? ????????????. id="+id));
            postListRepository.delete(iter);
        }
    }

    //????????? ??????
    @Transactional
    public void delete_post(Long bid, Long pid) {
        Post post = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("?????? ???????????? ????????????. id="+pid));
        //board????????? postlistid?????????
        PostList postlist = postListRepository.findById(post.getPostlistId()).orElseThrow(()->new IllegalArgumentException("?????? ???????????? ????????????. id="+post.getPostlistId()));
        Board board = boardRepository.findById(bid).orElseThrow(()->new IllegalArgumentException("?????? ???????????? ????????????. id="+bid));
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
        //comment??? ?????????
        List<Long> comment_id = post.getCommentId();
        for(Long iter_comment:comment_id) {
            commentRepository.delete(commentRepository.findById(iter_comment).orElseThrow(()->new IllegalArgumentException("?????? ????????? ????????????. id="+iter_comment)));
        }
        //postlist ?????????
        postListRepository.delete(postlist);
        //post?????????
        postRepository.delete(post);
    }
    //member?????? ?????? -> fixed?????????
    @Transactional
    public Long update_depart(Long id, String depart) {
        Address address = addressRepository.findById(id).orElseThrow(()->new IllegalArgumentException("?????? ???????????? ????????????. id="+id));
        Member member = membersRepository.findByAddress(address).orElseThrow(()->new IllegalArgumentException("?????? ????????? ????????????. address="+address));
        address.update_depart(depart);
        member.update(address);
        return member.getId();
    }

    @Transactional
    public Long update_position(Long id, String position) {
        Address address = addressRepository.findById(id).orElseThrow(()->new IllegalArgumentException("?????? ???????????? ????????????. id="+id));
        Member member = membersRepository.findByAddress(address).orElseThrow(()->new IllegalArgumentException("?????? ????????? ????????????. address="+address));
        address.update_position(position);
        member.update(address);
        return member.getId();
    }

    @Transactional
    public void delete_address(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(()->new IllegalArgumentException("?????? ???????????? ????????????. id="+id));
        addressRepository.delete(address);
    }
}
