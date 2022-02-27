package kr.co.ggabi.springboot.repository;
import kr.co.ggabi.springboot.domain.attachment.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    @Query("SELECT b FROM Attachment b ORDER BY b.id DESC")
    List<Attachment> findAllDesc();
}
