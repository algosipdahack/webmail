package kr.co.ggabi.springboot.repository;

import kr.co.ggabi.springboot.domain.mail.ReceivedWebMail;
import kr.co.ggabi.springboot.domain.users.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReceivedWebMailRepository extends JpaRepository<ReceivedWebMail, Long> {

    Optional<List<ReceivedWebMail>> findAllByUsernameAndMailId(String username, long idx);
    Optional<List<ReceivedWebMail>> findAllByIdAndMailId(long id, long idx);
    Optional<List<ReceivedWebMail>> findAllByUsername(String username);

}