package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.dto.*;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.service.AddressService;
import kr.co.ggabi.springboot.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/address")
public class AddressApiController {
    private final AddressService addressService;
    private final TokenProvider tokenProvider;

    //show fixed address(member information)
    @GetMapping("/fixed")
    public List<AddressResponseDto> show(){
        return addressService.findAllMember();
    }

    // return all personal address information(address 보여주기)
    @GetMapping("/personal")
    public List<AddressResponseDto> findAll (@RequestParam("writer") String writer){
        //personal 이므로 parentId가 해당 username을 가진 personal정보만 보여줘야 함
        String username = writer;

        return addressService.findAllDesc(username);
    }

    //create a personal address
    @PostMapping("/personal")
    public Long save(@RequestBody AddressSaveRequestDto requestDto) {
        //personal 이므로
        String username = requestDto.getWriter();
        System.out.println(username);
        return addressService.save(username,requestDto);
    }

    //modify personal address
    @PutMapping("/personal")
    public Long update(@RequestBody AddressUpdateRequestDto requestDto) {
        String username = requestDto.getWriter();
        return addressService.update(username,requestDto);
    }

    //remove personal address
    @PostMapping("/personal/delete")
    public void delete(@RequestParam("mid") List<String> mid){
        for (String id : mid) {
            Long lid = Long.parseLong(id);
            addressService.delete(lid);
        }
    }
}
