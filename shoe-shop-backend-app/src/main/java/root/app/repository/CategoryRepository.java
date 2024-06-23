package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.app.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
