package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.domain.posts.Post;
import kr.co.ggabi.springboot.domain.users.Address;
import kr.co.ggabi.springboot.dto.AddressSaveRequestDto;
import kr.co.ggabi.springboot.dto.AddressUpdateRequestDto;
import kr.co.ggabi.springboot.dto.PostUpdateRequestDto;
import kr.co.ggabi.springboot.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    AddressRepository addressRepository;
    @Transactional
    public Long save(AddressSaveRequestDto requestDto) {
        return addressRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long mid, AddressUpdateRequestDto requestDto) {
        Address address = addressRepository.findById(mid).orElseThrow(()->new IllegalArgumentException("해당 주소록이 없습니다. id="+mid));
        address.update(requestDto.getUsername(),requestDto.getNickname(),requestDto.getPhone(),requestDto.getEmail(),requestDto.getDepartment(),requestDto.getPosition(),requestDto.getCompany());
        return address.getId();
    }

    @Transactional
    public void delete(Long mid) {
        Address address = addressRepository.findById(mid).orElseThrow(()->new IllegalArgumentException("해당 주소록이 없습니다. id="+mid));
        addressRepository.delete(address);
    }
}
