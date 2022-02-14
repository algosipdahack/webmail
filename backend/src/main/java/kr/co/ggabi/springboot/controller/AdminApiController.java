package kr.co.ggabi.springboot.controller;
import kr.co.ggabi.springboot.domain.board.Board;
import kr.co.ggabi.springboot.dto.BoardSaveRequestDto;
import kr.co.ggabi.springboot.dto.BoardUpdateRequestDto;
import kr.co.ggabi.springboot.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminApiController {
    private final AdminService adminService;

    //modify board
    @PutMapping("/{bid}")
    public Long update(@PathVariable("bid") Long bid, @RequestBody BoardUpdateRequestDto requestDto) {
        return adminService.update(bid,requestDto);
    }
    //create a board
    @PostMapping("/board")
    public Long save(@RequestBody BoardSaveRequestDto requestDto) {
        return adminService.save(requestDto);
    }
    
    //remove board
    @DeleteMapping("/{bid}")
    public void delete_board(@PathVariable("bid") Long bid){
        adminService.delete_board(bid);
    }

    //remove post -> 해당 게시물 내에서 삭제
    @DeleteMapping("/{bid}/{pid}")
    public void delete_post(@PathVariable("bid") Long bid,@PathVariable("pid") Long pid) {
        adminService.delete_post(bid,pid);
    }

    //게시물 선택삭제
    @PostMapping("/{bid}/post")
    public void ajaxTest(@PathVariable("bid") Long bid, @RequestParam("pid") List<String> request) {
        System.out.println(request);
        for (String id : request) {
            Long lid = Long.parseLong(id);
            adminService.delete_post(bid, lid);
        }
    }
}
