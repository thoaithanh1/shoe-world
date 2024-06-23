package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import root.app.dto.UserDto;
import root.app.entity.User;
import root.app.service.UserService;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private static final String UPLOAD_DIR_USER = "E:/Angular/shoe-shop-backend-app/src/main/resources/static/images/user-image/";

    @Autowired
    private UserService userService;

    @GetMapping("/page")
    public ResponseEntity<?> findUserByPage(
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) Integer pageNum) {
        Page<UserDto> userPage = userService.findUserByPage(pageNum);
        if(userPage == null) {
            return new ResponseEntity<>("Không có người dùng nào ở trang này", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable("id") Long userId) {
        User user = userService.findUserById(userId);
        if(user == null) {
            return new ResponseEntity<>("Không có người dùng nào có mã " + userId, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestPart("user") User user,
                                         @RequestPart("file") MultipartFile multipartFile) throws IOException {
        User saveUser = userService.saveUser(user, multipartFile);
        return new ResponseEntity<>(saveUser, HttpStatus.CREATED);
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<Boolean> checkUserAuthenticated(Principal principal) {

        if (principal != null) {
            return ResponseEntity.status(200).body(true);
        }
        return ResponseEntity.status(403).body(false);
    }
}
