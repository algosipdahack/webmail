package kr.co.ggabi.springboot.repository;

import kr.co.ggabi.springboot.domain.users.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByUsername(String username);
    Optional<Address> findByPhone(String phone);
    Optional<Address> findByNickname(String nickname);

    @Query("SELECT p FROM Address p ORDER BY p.id DESC")
    List<Address> findAllDesc();
}
