package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import root.app.entity.UserAuth;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    UserAuth findByVerificationCode(String code);

    UserAuth findByResetPasswordToken(String resetPasswordToken);

    @Transactional
    @Modifying
    @Query("UPDATE UserAuth ua SET ua.verificationCode = null WHERE ua.verificationCode = :code")
    void updateRemoveVerify(@Param("code") String code);

    @Transactional
    @Modifying
    @Query("""
            UPDATE UserAuth ua SET ua.resetPasswordToken = null, ua.refreshTokenExpiration = null 
            WHERE ua.user.id = :userId
            """)
    void updateRemoveResetPasswordToken(@Param("userId") Long userId);
}
