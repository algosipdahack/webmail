package kr.co.ggabi.springboot.repository;

import kr.co.ggabi.springboot.domain.mail.WebMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface WebMailRepository extends JpaRepository<WebMail, Long> {
    Optional<WebMail> findFirstBySenderOrderByIdDesc(String sender);
    Optional<WebMail> findByReceiversAndMailId(String receivers, String mailId);
}
