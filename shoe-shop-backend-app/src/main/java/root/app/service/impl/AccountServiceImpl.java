package root.app.service.impl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import root.app.dto.RegisterDto;
import root.app.dto.RequestLogin;
import root.app.dto.ResponseLogin;
import root.app.entity.*;
import root.app.repository.*;
import root.app.security.jwt.JwtService;
import root.app.service.AccountService;
import root.app.util.MailSenderUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final JwtService jwtService;

    private final MailSenderUtil mailSenderUtil;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final RoleRepository roleRepository;

    private final RankingRepository rankingRepository;

    private final UserAuthRepository userAuthRepository;

    @Override
    public ResponseLogin login(RequestLogin requestLogin) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                requestLogin.getUsername(),
                                requestLogin.getPassword()));

        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.generateToken(requestLogin.getUsername());
            return ResponseLogin.builder()
                    .accessToken(accessToken)
                    .build();
        }

        throw new UsernameNotFoundException("Invalid User Request");
    }

    @Override
    public Map<String, String> register(HttpServletRequest request, RegisterDto userRegister) {
        Map<String, String> results = new HashMap<>();

        Role roleUser = roleRepository.findById(4L).get();
        Ranking rankBronze = rankingRepository.findById(1L).get();

        User user = new User(
                userRegister.getFirstName(),
                userRegister.getLastName(),
                userRegister.getEmail(),
                encoderPassword(userRegister.getPassword()));
        user.setStatus(false);
        user.addRole(roleUser);
        user.setRanking(rankBronze);
        User saveUser = userRepository.save(user);

        if (saveUser.getId() != null) {
            Cart cart = new Cart();
            cart.setUser(saveUser);
            cartRepository.save(cart);

            String verificationCode = RandomString.make(30);
            UserAuth userAuth = new UserAuth();
            userAuth.setVerificationCode(verificationCode);
            userAuth.setUser(saveUser);
            userAuthRepository.save(userAuth);
            sendEmailConfirmRegister(request, saveUser, verificationCode);

            results.put("message", "Tạo tài khoản thành công. Vui lòng vào email xác thực tài khoản để hoàn tất.");
            results.put("status", "OK");
        } else {
            results.put("message", "Đăng ký tài khoản thất bại");
            results.put("status", "FAILED");
        }

        return results;
    }

    @Override
    public Map<String, String> verificationAccount(String code) {
        Map<String, String> messages = new HashMap<>();
        UserAuth userAuth = userAuthRepository.findByVerificationCode(code);

        if (userAuth != null) {
            userRepository.updateStatusAfterVerifyAccount(userAuth.getUser().getId());
            userAuthRepository.updateRemoveVerify(code);
            messages.put("message", "Xác thực tài khoản thành công");
        } else {
            messages.put("message", "Tài khoản đã được xác thực");
        }

        return messages;
    }

    @Override
    public Map<String, String> requireResetPassword(String email) {
        Map<String, String> messages = new HashMap<>();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            String resetPasswordToken = RandomString.make(30);
            Date dateExpiration = new Date(System.currentTimeMillis() + 1000 * 60 * 5);

            UserAuth userAuth = new UserAuth();
            userAuth.setResetPasswordToken(resetPasswordToken);
            userAuth.setResetPasswordExpiration(dateExpiration);
            userAuth.setUser(user.get());
            userAuthRepository.save(userAuth);
            messages.put("message", "Gửi yêu cầu đặt lại mật khẩu thành công");
        } else {
            messages.put("message", "Email người dùng chưa đăng ký tài khoản trên hệ thống");
        }

        return messages;
    }

    @Override
    public Boolean checkResetPasswordToken(String resetPasswordToken) {
        UserAuth userAuth = userAuthRepository.findByResetPasswordToken(resetPasswordToken);

        if(userAuth != null) {
            Date dateExpiration = new Date(System.currentTimeMillis());
            return dateExpiration.before(userAuth.getResetPasswordExpiration());
        }
        return false;
    }

    @Override
    public Map<String, String> updatePasswordAfterCheckToken(Long userId, String password) {
        Map<String, String> messages = new HashMap<>();
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            User saveUser = new User();
            saveUser.setId(userId);
            saveUser.setPassword(encoderPassword(password));
            userRepository.save(saveUser);
            userAuthRepository.updateRemoveResetPasswordToken(userId);
            messages.put("message", "Đặt lại mật khẩu thành công");
        } else {
            messages.put("message", "Có lỗi xảy ra khi thay đổi mật khẩu");
        }
        return messages;
    }

    private String encoderPassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    private void sendEmailConfirmRegister(HttpServletRequest request, User user, String verificationCode) {
        try {
            String verificationAccountLink = request.getRequestURL().toString()
                                                     .replace(request.getServletPath(), "")
                                             + "/api/v1/account/verification_account?code=" + verificationCode;
            String subject = "Xác thực tài khoản";
            String content = "<p>Xin chào, " + user.getFirstName() + "</p>"
                             + "<p>Vừa có yêu cầu tạo tài khoản website ShoeWorld với email này</p>"
                             + "<p>Nếu là bạn hãy nhấn nút xác thực để hoàn tất thủ tục tạo tài khoản</p>"
                             + "<a href=\"" + verificationAccountLink + "\">Xác thực tài khoản</a>";
            mailSenderUtil.sendEmail(user.getEmail().trim(), subject, content);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
