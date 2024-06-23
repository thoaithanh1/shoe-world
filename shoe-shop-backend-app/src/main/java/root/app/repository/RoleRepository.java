package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.app.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
