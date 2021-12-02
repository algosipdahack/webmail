package kr.co.ggabi.springboot.repository;

import kr.co.ggabi.springboot.domain.mail.WebMail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebMailRepository extends JpaRepository<WebMail, Long> {

}
