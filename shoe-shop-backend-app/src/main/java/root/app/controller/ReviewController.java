package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import root.app.entity.Review;
import root.app.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{idProductDetail}")
    public ResponseEntity<?> findAllReviewProductDetail(@PathVariable("idProductDetail") Long idProductDetail) {
        List<Review> reviews = reviewService.findReviewByProductDetail(idProductDetail);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/count/{idProductDetail}")
    public ResponseEntity<Integer> countReviewByProductDetail(@PathVariable("idProductDetail") Long idProductDetail) {
        Integer countReview = reviewService.countReviewByProductDetail(idProductDetail);
        return ResponseEntity.ok(countReview);
    }

    @GetMapping("/count-per-rating/{rating}/{idProductDetail}")
    public ResponseEntity<Integer> countReviewByRatingAndProductDetail(@PathVariable("rating") Integer rating,
                                                                       @PathVariable("idProductDetail") Long idProductDetail) {
        Integer countReview = reviewService.countReviewByRatingAndProductDetail(rating, idProductDetail);
        return ResponseEntity.ok(countReview);
    }

    @GetMapping("/check-reviewed/{orderId}/{productDetailId}/{userId}")
    public ResponseEntity<Boolean> checkProductReviewed(@PathVariable("orderId") Long orderId,
                                                        @PathVariable("productDetailId") Long productDetailId,
                                                        @PathVariable("userId") Long userId) {
        Boolean result = reviewService.checkProductReviewed(orderId, productDetailId, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-average/{idProductDetail}")
    public ResponseEntity<?> getAverageRating(@PathVariable("idProductDetail") Long idProductDetail) {
        Double averageRating = reviewService.getAverageRating(idProductDetail);
        return ResponseEntity.ok(averageRating);
    }

    @PostMapping
    public ResponseEntity<?> saveReview(@RequestPart("review") Review review,
                                        @RequestPart("file") MultipartFile file) {
        Review saveReview = reviewService.saveReview(review, file);
        return ResponseEntity.ok(saveReview);
    }
}
