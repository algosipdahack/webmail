package kr.co.ggabi.springboot.controller;

import kr.co.ggabi.springboot.dto.*;
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
    private final MemberService memberService;

    // return all fixed address information(member 보여주기)
    @GetMapping
    public List<MemberResponseDto> findAll (){
        return memberService.findAllDesc();
    }

    //create a personal address
    @PostMapping("/{mid}")
    public Long save(@PathVariable("mid") Long mid, @RequestBody AddressSaveRequestDto requestDto) {
        //personal 이므로
        requestDto.setParentId(mid);
        return addressService.save(requestDto);
    }

    //modify comment
    @PutMapping("/{mid}")
    public Long update(@PathVariable("mid") Long mid, @RequestBody AddressUpdateRequestDto requestDto) {
        return addressService.update(mid,requestDto);
    }

    //remove comment
    @DeleteMapping("/{mid}")
    public void delete(@PathVariable("mid") Long mid){
        addressService.delete(mid);
    }
}
