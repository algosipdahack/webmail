package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.users.Address;
import kr.co.ggabi.springboot.dto.AddressSaveRequestDto;
import kr.co.ggabi.springboot.dto.MembersSaveResponseDto;
import kr.co.ggabi.springboot.repository.AddressRepository;
import kr.co.ggabi.springboot.repository.MembersRepository;
import kr.co.ggabi.springboot.dto.MembersSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@RequiredArgsConstructor
@Service
public class CreateMemberService {
    private final MembersRepository membersRepository;
    private final AddressRepository addressRepository;

    @Value("${james.dir}")
    private String dir;
    @Value("${mailServer.domain}")
    String domain;

    @Transactional
    public MembersSaveResponseDto save(MembersSaveRequestDto membersSaveRequestDto) throws IOException {
        AddressSaveRequestDto addressSaveRequestDto = new AddressSaveRequestDto(membersSaveRequestDto.getUsername(),membersSaveRequestDto.getNickname(),membersSaveRequestDto.getPhone(),membersSaveRequestDto.getEmail(),membersSaveRequestDto.getDepartment(),membersSaveRequestDto.getPosition(),membersSaveRequestDto.getCompany());
        String username = membersSaveRequestDto.toEntity().getUsername();
        String password = membersSaveRequestDto.toEntity().getPassword();
        System.out.println(dir + " AddUser " + username + "@" + domain + " " + password.substring(6));
        Process process = Runtime.getRuntime().exec(dir + " AddUser " + username + "@" + domain + " " + password.substring(6));
        MembersSaveResponseDto res = new MembersSaveResponseDto("fail", "error");
        System.out.println(username);
        if(membersRepository.findByUsername(username).isPresent()){
            res.setMessage("중복 ID입니다.");
        } else if (addressRepository.findByPhone( addressSaveRequestDto.toEntity().getPhone()).isPresent()){
            res.setMessage("중복 전화번호입니다.");
        } else {
            res.setStatus("success");
            res.setMessage("성공");
            Address address = addressRepository.save(addressSaveRequestDto.toEntity());
            membersSaveRequestDto.setAddress(address);
            membersRepository.save(membersSaveRequestDto.toEntity());
        }
        return res;
    }
}
