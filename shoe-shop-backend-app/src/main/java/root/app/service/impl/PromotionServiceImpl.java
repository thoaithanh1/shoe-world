package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import root.app.contant.PromotionContant;
import root.app.entity.Promotion;
import root.app.exception.ObjectNotFoundException;
import root.app.repository.PromotionRepository;
import root.app.service.PromotionService;
import root.app.util.GenerateStringUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public List<Promotion> findAllPromotion() {
        return promotionRepository.findAll();
    }

    @Override
    public Promotion findPromotionById(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Không tìm thấy khuyến mại có mã " + id));
    }

    @Override
    public List<Promotion> filterPromotion(String keyword, String status, String method, String operator) {
        Date today = new Date();
        Integer discountMethod = null;
        String allMethod = "";
        Boolean statusPromotion = null;
        String skip = "skip";
        if (status.equals("true")) {
            statusPromotion = true;
            skip = "true";
        } else if (status.equals("false")) {
            statusPromotion = false;
            skip = "false";
        }
        if (method.equals("1")) {
            discountMethod = 1;
            allMethod = "1";
        } else if (method.equals("2")) {
            discountMethod = 2;
            allMethod = "2";
        }
        return promotionRepository.filterPromotion(keyword, statusPromotion, skip, discountMethod, allMethod, today, operator);
    }

    @Override
    public Page<Promotion> findAllPromotionByPage(Integer pageNum, Integer qtyPerPage) {
        Pageable pageable = PageRequest.of(pageNum, qtyPerPage);
        return promotionRepository.findAll(pageable);
    }

    @Override
    public Promotion findPromotionByDateAndCondition(BigDecimal condition) {
        return promotionRepository.findPromotionByDateAndCondition(new Date(), condition);
    }

    @Override
    public String savePromotion(Promotion promotion) {
        boolean isUpdating = promotion.getId() != null;

        if (isUpdating) {
            Promotion promotionDB = promotionRepository.findById(promotion.getId()).get();
            promotion.setCode(promotionDB.getCode());
            promotion.setCreatedBy(promotionDB.getCreatedBy());
            promotion.setCreatedDate(promotionDB.getCreatedDate());
        } else {
            String code = "KM" + GenerateStringUtil.generateString(10);
            promotion.setCode(code);
        }

        if (promotion.getDiscountMethod().equals(PromotionContant.DISCOUNT_METHOD_PRODUCT)) {
            promotion.setConditionsApply(BigDecimal.ZERO);
        }
        if (promotion.getDiscountType().equals(PromotionContant.DISCOUNT_TYPE_CASH)) {
            promotion.setMaxDiscountAmount(promotion.getDiscountValue());
        }
        return promotionRepository.save(promotion).getId() > 0 ? "OK" : "FAILED";
    }
}
