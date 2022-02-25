package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.board.Board;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.domain.posts.PostList;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.dto.*;
import kr.co.ggabi.springboot.repository.*;
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
    private final MembersRepository membersRepository;

    @Transactional
    public PostList save(PostListSaveRequestDto requestDto) {
        String writerName = null;
        List<Member> members = membersRepository.findAllDesc();
        for(Member iter: members) {
            if(iter.getUsername().equals(requestDto.getWriter())) {
                writerName = iter.getAddress().getNickname();
                break;
            }
        }
        requestDto.setWriterName(writerName);
        return postListRepository.save(requestDto.toEntity());
    }
    @Transactional
    public PostList savePostId(Long pid) {
        Long postlistId = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid)).getPostlistId();
        PostList postList = postListRepository.findById(postlistId).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+postlistId));
        postList.postId(pid);
        return postList;
    }
    @Transactional
    public PostList update(Long bid, Long pid, PostListUpdateRequestDto requestDto) {
        Long postlistId = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid)).getPostlistId();
        PostList postList = postListRepository.findById(postlistId).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+postlistId));
        postList.update(requestDto.getTitle(),requestDto.getIs_notice());
        return postList;
    }

    public PostListResponseDto findById(Long bid, Long pid) {
        Long postlistId = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid)).getPostlistId();
        PostList postList = postListRepository.findById(postlistId).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+postlistId));
        return new PostListResponseDto(postList);
    }

    @Transactional(readOnly = true) // 조회기능
    public List<PostListResponseDto> findAllDesc() {
        List<PostListResponseDto> tmp =  postListRepository.findAllDesc().stream()
                .map(PostListResponseDto::new)// == .map(posts->new PostsListResponseDto(posts))
                .collect(Collectors.toList());

        List<Board> board = boardRepository.findAllDesc();
        for(PostListResponseDto iter_list:tmp){
            for(Board iter:board){
                int flag = 0;
                List<Long> list = iter.getPostlistId();
                for(Long list_id:list) {
                    if(list_id == iter_list.getId()) {
                        iter_list.setBoard(iter);
                        flag = 1;
                        break;
                    }
                }
                if(flag == 1)
                    break;
            }
        }
        return tmp;
    }

    @Transactional
    public void delete(Long bid,Long pid) {
        Long postlistId = postRepository.findById(pid).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다. id="+pid)).getPostlistId();
        PostList postList = postListRepository.findById(postlistId).orElseThrow(()->new IllegalArgumentException("해당 게시판이 없습니다. id="+postlistId));
        postListRepository.delete(postList);
    }
}
