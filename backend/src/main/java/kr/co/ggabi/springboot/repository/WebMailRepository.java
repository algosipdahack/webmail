package kr.co.ggabi.springboot.repository;

import kr.co.ggabi.springboot.domain.mail.ReceivedWebMail;
import kr.co.ggabi.springboot.domain.mail.WebMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface WebMailRepository extends JpaRepository<WebMail, Long> {
    Optional<WebMail> findFirstBySenderOrderByIdDesc(String sender);
    Optional<WebMail> findFirstByIsReceivedAndReceiverContainsOrderByIdDesc(boolean isReceived, String from);
    List<WebMail> findBySenderAndHaveToApproval(String sender, boolean haveToApproval);
    List<WebMail> findBySenderAndIsAcceptApproval(String sender, boolean isAcceptApproval);
    WebMail findBySenderAndMailId(String sender, int webMailId);

    Optional<WebMail> findFirstByOrderByIdDesc();
    Optional<WebMail> findByReceiverAndMailId(String receiver, int mailId);
    Optional<List<WebMail>> findByReceiverContainsAndMailIdAndIsReceivedTrue(String receiver, int mailId);
    WebMail findByMailId(int mailId);

    List<WebMail> findAll();

    Optional<WebMail> findOneById(Long id);

    WebMail findBySenderAndDateAndIsReceivedTrue(String sender, Date date);

}
