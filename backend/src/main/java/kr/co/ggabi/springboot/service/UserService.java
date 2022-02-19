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
            Member member = membersRepository.findById(mid).orElseThrow(()->new IllegalArgumentException("해당 멤버가 없습니다. id="+mid));
            Address address = member.getAddress();
            Runtime.getRuntime().exec(dir + " RemoveUser " + address.getUsername() + "@" + domain);
            Runtime.getRuntime().exec(dir + " DeleteUserMailboxes " + address.getUsername() + "@" + domain);
            addressRepository.delete(address);
            membersRepository.delete(member);
            return "success";
        } catch (Exception e){
            return "fail";
        }
    }
}
