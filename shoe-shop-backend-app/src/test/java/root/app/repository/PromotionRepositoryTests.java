package root.app.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import root.app.entity.Promotion;

import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PromotionRepositoryTests {

    @Autowired
    private PromotionRepository promotionRepository;

    @Test
    public void findPromotionByCodeOrName() {
        List<Promotion> promotions = promotionRepository.findPromotionsByCodeOrName("20");
        promotions.forEach(System.out::println);
        assertThat(promotions.size()).isGreaterThan(0);
    }

    @Test
    public void findPromotionByStatus() {
        List<Promotion> promotions = promotionRepository.findPromotionsByStatus(false);
        promotions.forEach(System.out::println);
        assertThat(promotions.size()).isGreaterThan(0);
    }

    @Test
    public void findPromotionByExpire() {
        Date today = new Date();
        List<Promotion> promotions = promotionRepository.findPromotionByExpire(today);
        promotions.forEach(System.out::println);
        assertThat(promotions.size()).isGreaterThan(0);
    }

    @Test
    public void findPromotionByEffect() {
        Date today = new Date();
        List<Promotion> promotions = promotionRepository.findPromotionByActivated(today, "<");
        promotions.forEach(System.out::println);
        assertThat(promotions.size()).isGreaterThan(0);
    }

//    @Test
//    public void filterPromotion() {
//        Date today = new Date();
//        List<Promotion> promotions = promotionRepository.filterPromotion("", null, today, "");
//        promotions.forEach(System.out::println);
//        assertThat(promotions.size()).isGreaterThan(0);
//    }
}
