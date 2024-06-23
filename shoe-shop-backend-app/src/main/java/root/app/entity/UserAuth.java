package root.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user_auth")
public class UserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "reset_password_expiration")
    private Date resetPasswordExpiration;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "refresh_token_expiration")
    private Date refreshTokenExpiration;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
