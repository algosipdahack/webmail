package kr.co.ggabi.springboot.repository;

import kr.co.ggabi.springboot.domain.board.Board;
import kr.co.ggabi.springboot.domain.users.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByPhone(String phone);
    Optional<Address> findByNickname(String nickname);

    @Query("SELECT p FROM Address p where p.parentId = :parentId ORDER BY p.id DESC")
    List<Address> findAllDesc(@Param("parentId") Long parentId);

    //parentId is null == member
    @Query("SELECT p FROM Address p where p.parentId is null ORDER BY p.id DESC")
    List<Address> findAllMember();

    @Query("SELECT b FROM Address b ORDER BY b.id DESC")
    List<Address> findAll();
}
