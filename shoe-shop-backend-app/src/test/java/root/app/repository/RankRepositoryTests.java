package root.app.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import root.app.contant.RankingContant;
import root.app.entity.Ranking;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RankRepositoryTests {

    @Autowired
    private RankingRepository rankingRepository;

    @Test
    public void addRank() {
        Ranking bronze = new Ranking();
        bronze.setName(RankingContant.BRONZE.name());
        bronze.setCondition(RankingContant.BRONZE.condition);
        bronze.setDiscount(RankingContant.BRONZE.discount);

        rankingRepository.save(bronze);
    }

    @Test
    public void addMultipRank() {
        Ranking silver = new Ranking();
        silver.setName(RankingContant.SILVER.name());
        silver.setCondition(RankingContant.SILVER.condition);
        silver.setDiscount(RankingContant.SILVER.discount);

        Ranking gold = new Ranking();
        gold.setName(RankingContant.GOLD.name());
        gold.setCondition(RankingContant.GOLD.condition);
        gold.setDiscount(RankingContant.GOLD.discount);

        Ranking platinum = new Ranking();
        platinum.setName(RankingContant.PLATINUM.name());
        platinum.setCondition(RankingContant.PLATINUM.condition);
        platinum.setDiscount(RankingContant.PLATINUM.discount);

        Ranking diamond = new Ranking();
        diamond.setName(RankingContant.DIAMOND.name());
        diamond.setCondition(RankingContant.DIAMOND.condition);
        diamond.setDiscount(RankingContant.DIAMOND.discount);

        List<Ranking> rankings = rankingRepository.saveAll(List.of(silver, gold, platinum, diamond));

        assertThat(rankings.size()).isEqualTo(4);
    }
}
