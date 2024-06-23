package root.app.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import root.app.dto.RegisterDto;
import root.app.dto.RequestLogin;
import root.app.dto.ResponseLogin;
import root.app.dto.UserDto;
import root.app.service.AccountService;
import root.app.service.UserService;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> profileUser() {

        UserDto user = userService.getUserPrincipal();
        return ResponseEntity.ok(Objects.requireNonNullElse(user, "User is not authenticated"));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLogin> login(@RequestBody RequestLogin requestLogin) {
        ResponseLogin responseLogin = accountService.login(requestLogin);
        return ResponseEntity.ok(responseLogin);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(HttpServletRequest request, @RequestBody RegisterDto registerDto) {
        Map<String, String> register = accountService.register(request, registerDto);
        return ResponseEntity.ok(register);
    }

    @GetMapping("/verification_account")
    public ResponseEntity<Map<String, String>> verificationAccount(@RequestParam("code") String code) {
        Map<String, String> verificationAccount = accountService.verificationAccount(code);
        return ResponseEntity.ok(verificationAccount);
    }

    @GetMapping("/request_reset_password")
    public ResponseEntity<Map<String, String>> resetPassword() {

        return null;
    }
}
