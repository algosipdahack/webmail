package kr.co.ggabi.springboot.repository;

import kr.co.ggabi.springboot.domain.comments.Comment;
import kr.co.ggabi.springboot.domain.users.Address;
import kr.co.ggabi.springboot.domain.users.Authority;
import kr.co.ggabi.springboot.domain.users.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MembersRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByAddress(Address address);
    @EntityGraph(attributePaths = "authority")
    Optional<Member> findOneWithAuthorityByUsername(String username);
    List<Member> findAllByAuthority(Authority authority);

    @Query("SELECT p FROM Member p ORDER BY p.id DESC")
    List<Member> findAllDesc();
}
