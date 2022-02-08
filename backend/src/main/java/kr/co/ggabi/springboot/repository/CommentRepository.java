package kr.co.ggabi.springboot.repository;

import kr.co.ggabi.springboot.domain.comments.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("SELECT c FROM Comment c ORDER BY c.id DESC")
    List<Comment> findAllDesc();

}
