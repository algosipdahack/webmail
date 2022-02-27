package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.users.Address;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.dto.*;
import kr.co.ggabi.springboot.repository.AddressRepository;
import kr.co.ggabi.springboot.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final MembersRepository membersRepository;
    @Transactional
    public Long save(String username, AddressSaveRequestDto requestDto) {
        List<Member> member = membersRepository.findAllDesc();
        Long mid = null;
        for (Member iter:member){
            if(iter.getUsername().equals(username)){
                mid = iter.getId();
                break;
            }
        }
        System.out.println(mid);
        requestDto.setParentId(mid);
        return addressRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(String username, AddressUpdateRequestDto requestDto) {
        Address address = addressRepository.findById(requestDto.getId()).orElseThrow(()->new IllegalArgumentException("해당 주소록이 없습니다. id="+requestDto.getId()));
        address.update(requestDto.getNickname(),requestDto.getPhone(),requestDto.getEmail(),requestDto.getDepartment(),requestDto.getPosition(),requestDto.getCompany());
        return address.getId();
    }

    @Transactional
    public void delete(Long mid) {
        Address address = addressRepository.findById(mid).orElseThrow(()->new IllegalArgumentException("해당 주소록이 없습니다. mid="+mid));
        addressRepository.delete(address);
    }
    @Transactional(readOnly = true) // 조회기능
    public List<AddressResponseDto>  findAllDesc(String username) {
        Long parentId = membersRepository.findByUsername(username).orElseThrow(()->new IllegalArgumentException("해당 주소록이 없습니다. username="+username)).getId();
        return addressRepository.findAllDesc(parentId).stream()
                .map(AddressResponseDto::new)// == .map(Board->new BoardListResponseDto(board))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true) // 조회기능
    public List<AddressResponseDto> findAllMember() {
        return addressRepository.findAllMember().stream()
                .map(AddressResponseDto::new)// == .map(Board->new BoardListResponseDto(board))
                .collect(Collectors.toList());
    }
}
