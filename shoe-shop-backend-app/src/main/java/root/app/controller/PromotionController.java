package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.app.entity.Promotion;
import root.app.service.PromotionService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/promotion")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping
    public ResponseEntity<?> findAllPromotion() {
        List<Promotion> promotions = promotionService.findAllPromotion();
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/{promotionId}")
    public ResponseEntity<?> findPromotionById(@PathVariable("promotionId") Long promotionId) {
        Promotion promotion = promotionService.findPromotionById(promotionId);
        return ResponseEntity.ok(promotion);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterPromotion(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            @RequestParam(value = "method", required = false, defaultValue = "") String method,
            @RequestParam(value = "operator", required = false, defaultValue = "") String operator) {
        return ResponseEntity.ok(promotionService.filterPromotion(keyword, status, method, operator));
    }

    @GetMapping("/get-promotion-apply/{conditionApply}")
    public ResponseEntity<Promotion> findPromotionByCondition(
            @PathVariable("conditionApply") BigDecimal conditionApply ) {
        Promotion promotion = promotionService.findPromotionByDateAndCondition(conditionApply);
        return ResponseEntity.ok(promotion);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> savePromotion(@RequestBody Promotion promotion) {
        Map<String, String> result = new HashMap<>();
        result.put("status", promotionService.savePromotion(promotion));
        return ResponseEntity.ok(result);
    }
}
