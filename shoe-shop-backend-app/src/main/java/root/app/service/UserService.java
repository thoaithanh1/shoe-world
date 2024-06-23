package root.app.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import root.app.dto.UserDto;
import root.app.entity.User;

import java.io.IOException;
import java.util.Optional;

public interface UserService {

    Page<UserDto> findUserByPage(Integer pageNum);

    UserDto getUserPrincipal();

    User findUserById(Long userId);

    User saveUser(User user, MultipartFile multipartFile) throws IOException;

    void deleteUser(Long userId);
}
