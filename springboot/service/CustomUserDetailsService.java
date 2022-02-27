package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.repository.MembersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MembersRepository membersRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username){
        return membersRepository.findOneWithAuthorityByUsername(username)
                .map(member -> createUser(username, member))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private User createUser(String username, Member member){
        /*if(!member.getIsPermitted()){
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        }*/
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());
        return new User(member.getUsername(), member.getPassword(), Collections.singleton(grantedAuthority));
    }
}
