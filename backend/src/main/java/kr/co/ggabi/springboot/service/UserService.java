package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.users.Address;
import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.repository.AddressRepository;
import kr.co.ggabi.springboot.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final MembersRepository membersRepository;
    private final AddressRepository addressRepository;

    @Value("${james.dir}")
    private String dir;
    @Value("${mailServer.domain}")
    String domain;

    public String delete(Long mid){
        try{
            Address address = addressRepository.findById(mid).orElseThrow(()->new IllegalArgumentException("해당 주소록이 없습니다. id="+mid));
            Member member = membersRepository.findByAddress(address).orElseThrow(()->new IllegalArgumentException("해당 멤버가 없습니다. address="+address));
            Runtime.getRuntime().exec(dir + " RemoveUser " + member.getUsername() + "@" + domain);
            Runtime.getRuntime().exec(dir + " DeleteUserMailboxes " + member.getUsername() + "@" + domain);
            membersRepository.delete(member);
            addressRepository.delete(address);
            return "success";
        } catch (Exception e){
            return "fail";
        }
    }
}
