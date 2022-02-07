package kr.co.ggabi.springboot.service;
import kr.co.ggabi.springboot.domain.board.Board;
import kr.co.ggabi.springboot.dto.BoardListResponseDto;
import kr.co.ggabi.springboot.dto.BoardResponseDto;
import kr.co.ggabi.springboot.dto.BoardSaveRequestDto;
import kr.co.ggabi.springboot.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public Long save(BoardSaveRequestDto requestDto) {
        return boardRepository.save(requestDto.toEntity()).getId();
    }


    public BoardResponseDto findById(Long id) {
        Board entity = boardRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        return new BoardResponseDto(entity);
    }

    @Transactional(readOnly = true) // 조회기능
    public List<BoardListResponseDto> findAllDesc() {
        return boardRepository.findAllDesc().stream()
                .map(BoardListResponseDto::new)// == .map(Board->new BoardListResponseDto(board))
                .collect(Collectors.toList());
    }

}
