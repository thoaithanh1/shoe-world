package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import root.app.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.status = true WHERE u.id = :userId")
    void updateStatusAfterVerifyAccount(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :userId")
    void updatePasswordAfterCheckToken(@Param("userId") Long userId,
                                       @Param("newPassword") String newPassword);
}
