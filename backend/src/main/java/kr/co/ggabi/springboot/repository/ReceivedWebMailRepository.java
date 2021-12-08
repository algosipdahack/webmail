package kr.co.ggabi.springboot.repository;

import kr.co.ggabi.springboot.domain.mail.ReceivedWebMail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivedWebMailRepository extends JpaRepository<ReceivedWebMail, Long> {


}