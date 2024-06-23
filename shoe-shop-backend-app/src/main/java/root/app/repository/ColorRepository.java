package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.app.entity.Color;

public interface ColorRepository extends JpaRepository<Color, Long> {
}
