package kr.co.ggabi.springboot.repository;

import kr.co.ggabi.springboot.domain.users.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembersRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    @EntityGraph(attributePaths = "authority")
    Optional<Member> findOneWithAuthorityByUsername(String username);
}
