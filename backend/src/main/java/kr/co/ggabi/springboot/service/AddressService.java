package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.domain.users.Address;
import kr.co.ggabi.springboot.dto.*;
import kr.co.ggabi.springboot.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    AddressRepository addressRepository;

    @Transactional
    public Long save(String username, AddressSaveRequestDto requestDto) {
        Long mid = addressRepository.findByUsername(username).get().getId();
        requestDto.setParentId(mid);
        return addressRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(String username, AddressUpdateRequestDto requestDto) {
        Address address = addressRepository.findByUsername(username).orElseThrow(()->new IllegalArgumentException("해당 주소록이 없습니다. username="+username));
        address.update(requestDto.getUsername(),requestDto.getNickname(),requestDto.getPhone(),requestDto.getEmail(),requestDto.getDepartment(),requestDto.getPosition(),requestDto.getCompany());
        return address.getId();
    }

    @Transactional
    public void delete(Long mid) {
        Address address = addressRepository.findById(mid).orElseThrow(()->new IllegalArgumentException("해당 주소록이 없습니다. mid="+mid));
        addressRepository.delete(address);
    }
    @Transactional(readOnly = true) // 조회기능
    public List<AddressResponseDto>  findAllDesc(String username) {
        Long parentId = addressRepository.findByUsername(username).orElseThrow(()->new IllegalArgumentException("해당 주소록이 없습니다. username="+username)).getId();
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
