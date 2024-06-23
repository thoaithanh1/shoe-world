package root.app.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import root.app.entity.Ranking;
import root.app.entity.Role;
import root.app.entity.User;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void addUserWithAdminRole() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Role role = entityManager.find(Role.class, 3);

        User user = new User();
        user.setId(1L);
        user.setFirstName("Kiên");
        user.setLastName("Đỗ");
        user.setEmail("dohongkien2003@gmail.com");
        user.setPhoneNumber("0969951201");
        user.setPassword(passwordEncoder.encode("kien2003"));
        user.setCreatedDate(new Date());
        user.setStatus(true);
        user.addRole(role);

        User saveUser = userRepository.save(user);

        assertThat(saveUser.getId()).isEqualTo(1);
    }

    @Test
    public void addUserWithUserRole() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Role role = entityManager.find(Role.class, 4);

        User user = new User();
        user.setFirstName("Linh");
        user.setLastName("Nguyễn");
        user.setEmail("nguyenngoclinh@gmail.com");
        user.setPhoneNumber("0369239438");
        user.setPassword(passwordEncoder.encode("linh2003"));
        user.addRole(role);

        User saveUser = userRepository.save(user);

        assertThat(saveUser.getId()).isEqualTo(2);
    }

    @Test
    public void addUserWithTwoRole() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Role roleAdmin = entityManager.find(Role.class, 3);
        Role roleUser = entityManager.find(Role.class, 4);
        Ranking rankBronze = entityManager.find(Ranking.class, 1);

        User user = new User();
        user.setFirstName("Lan");
        user.setLastName("Nguyễn");
        user.setEmail("nguyenphuonglan@gmail.com");
        user.setPhoneNumber("0934973458");
        user.setPassword(passwordEncoder.encode("lan2003"));
        user.addRole(roleAdmin);
        user.addRole(roleUser);
        user.setRanking(rankBronze);

        User saveUser = userRepository.save(user);

        assertThat(saveUser.getId()).isGreaterThan(0);
    }
}
