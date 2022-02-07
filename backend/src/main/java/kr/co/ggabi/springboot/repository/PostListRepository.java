package kr.co.ggabi.springboot.repository;
import kr.co.ggabi.springboot.domain.posts.PostList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostListRepository extends JpaRepository<PostList,Long>{
    @Query("SELECT p FROM Postlist p ORDER BY p.id DESC")
    List<PostList> findAllDesc();
}