package root.app.service;

import org.springframework.data.domain.Page;
import root.app.entity.Promotion;

import java.math.BigDecimal;
import java.util.List;

public interface PromotionService {

    List<Promotion> findAllPromotion();

    Promotion findPromotionById(Long id);

    List<Promotion> filterPromotion(String keyword, String status, String method, String operator);

    Page<Promotion> findAllPromotionByPage(Integer pageNum, Integer qtyPerPage);

    Promotion findPromotionByDateAndCondition(BigDecimal condition);

    String savePromotion(Promotion promotion);
}
