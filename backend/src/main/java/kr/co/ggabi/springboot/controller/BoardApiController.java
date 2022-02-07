package kr.co.ggabi.springboot.controller;
import kr.co.ggabi.springboot.dto.BoardListResponseDto;
import kr.co.ggabi.springboot.dto.BoardResponseDto;
import kr.co.ggabi.springboot.dto.BoardSaveRequestDto;
import kr.co.ggabi.springboot.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardApiController {
    private final BoardService boardService;

    //create a board
    @PostMapping
    public Long save(@RequestBody BoardSaveRequestDto requestDto) {
        return boardService.save(requestDto);
    }

    //return all board information
    @GetMapping
    public List<BoardListResponseDto> findAll (){
        return boardService.findAllDesc();
    }

    //return specific board information
    @GetMapping("/{bid}")
    public BoardResponseDto findById (@PathVariable("bid") Long id){
        return boardService.findById(id);
    }

}
