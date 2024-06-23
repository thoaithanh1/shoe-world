package root.app.service;

import jakarta.servlet.http.HttpServletRequest;
import root.app.dto.RegisterDto;
import root.app.dto.RequestLogin;
import root.app.dto.ResponseLogin;

import java.util.Map;

public interface AccountService {

    ResponseLogin login(RequestLogin requestLogin);

    Map<String, String> register(HttpServletRequest request, RegisterDto registerDto);

    Map<String, String> verificationAccount(String code);

    Map<String, String> requireResetPassword(String email);

    Boolean checkResetPasswordToken(String resetPasswordToken);

    Map<String, String> updatePasswordAfterCheckToken(Long userId, String password);
}
