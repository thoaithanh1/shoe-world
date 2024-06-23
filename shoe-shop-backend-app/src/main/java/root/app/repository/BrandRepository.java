package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.app.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
