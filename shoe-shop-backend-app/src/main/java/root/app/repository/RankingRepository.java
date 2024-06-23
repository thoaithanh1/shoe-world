package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.app.entity.Ranking;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
}
