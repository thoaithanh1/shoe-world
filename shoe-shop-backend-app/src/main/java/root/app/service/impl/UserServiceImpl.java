package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import root.app.dto.UserDto;
import root.app.entity.User;
import root.app.exception.ObjectNotFoundException;
import root.app.mapper.CustomMapper;
import root.app.repository.UserRepository;
import root.app.service.UserService;
import root.app.util.FileUploadUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final String UPLOAD_DIR_USER = "E:/Angular/shoe-shop-frontend-app/src/assets/image-data/user-image/";

    private static final Integer USER_PER_PAGE = 5;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomMapper customMapper;

    @Override
    public Page<UserDto> findUserByPage(Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum, USER_PER_PAGE);
        Page<User> userPage = userRepository.findAll(pageable);
        List<UserDto> userDtos = userRepository.findAll(pageable)
                .stream()
                .map(u -> customMapper.convertUserToDto(u))
                .collect(Collectors.toList());
        return new PageImpl<>(userDtos, pageable, userPage.getTotalElements());
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Không tìm thấy người dùng có mã " + userId));
    }

    @Override
    public UserDto getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
            if (user.isPresent()) {
                User userDB = user.get();
                return customMapper.convertUserToDto(userDB);
            }
        }

        return null;
    }

    @Override
    public User saveUser(User user, MultipartFile multipartFile) throws IOException {
        boolean isUpdating = user.getId() != null;

        if (isUpdating) {
            Optional<User> userDB = userRepository.findById(user.getId());
            if (userDB.isPresent()) {
                if (user.getPassword() == null) {
                    user.setPassword(userDB.get().getPassword());
                } else {
                    encoderPassword(user);
                }
                user.setCreatedDate(userDB.get().getCreatedDate());
                user.setRanking(userDB.get().getRanking());
                user.setStatus(userDB.get().getStatus());
                user.setAvatar(userDB.get().getAvatar());
            }
        } else {
            encoderPassword(user);
        }
        return checkStateUser(user, multipartFile);

//        if (!multipartFile.isEmpty()) {
//            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//            user.setAvatar(fileName);
//            encoderPassword(user);
//            User saveUser = userRepository.save(user);
//
//            String uploadDir = UPLOAD_DIR_USER + saveUser.getId();
//            FileUploadUtil.cleanDir(uploadDir);
//            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//
//            return saveUser;
//        } else {
//            if (user.getAvatar().isEmpty()) user.setAvatar(null);
//            return userRepository.save(user);
//        }
    }

    private User checkStateUser(User user, MultipartFile multipartFile) throws IOException {
        String fileName = null;
        if (!multipartFile.isEmpty()) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setAvatar(fileName);
        }
        User saveUser = userRepository.save(user);

        if (fileName != null) {
            String uploadDir = UPLOAD_DIR_USER + saveUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        return saveUser;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    private void encoderPassword(User user) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
    }
}
