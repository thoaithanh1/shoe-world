package root.app.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import root.app.contant.RoleContant;
import root.app.entity.Role;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void addRole() {
        Role admin = new Role();
        admin.setName(RoleContant.ROLE_ADMIN);
        admin.setDescription("Đây là vai trò của người quản lý website");

        Role user = new Role();
        user.setName(RoleContant.ROLE_USER);
        user.setDescription("Đây là vai trò của khách hàng khi đăng ký tài khoản trên hệ thống");

        List<Role> roles = roleRepository.saveAll(List.of(admin, user));

        assertThat(roles.size()).isEqualTo(2);
    }
}
