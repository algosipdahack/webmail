package kr.co.ggabi.springboot.repository;
import kr.co.ggabi.springboot.domain.posts.PostList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostListRepository extends JpaRepository<PostList,Long>{
    @Query("SELECT p FROM PostList p ORDER BY p.id DESC")
    List<PostList> findAllDesc();

    @Transactional
    @Modifying
    @Query("UPDATE PostList p set p.hits = p.hits + 1 where p.id = :questionId")
    void updateShowCount(@Param("questionId") Long questionId);
}