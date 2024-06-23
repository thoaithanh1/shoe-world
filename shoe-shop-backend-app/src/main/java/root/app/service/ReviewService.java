package root.app.service;

import org.springframework.web.multipart.MultipartFile;
import root.app.entity.Review;
import java.util.List;

public interface ReviewService {

    List<Review> findReviewByProductDetail(Long productDetailId);

    Integer countReviewByProductDetail(Long productDetailId);

    Integer countReviewByRatingAndProductDetail(Integer rating, Long productDetailId);

    Boolean checkProductReviewed(Long orderId, Long productDetailId, Long userId);

    Double getAverageRating(Long idProductDetail);

    Review saveReview(Review review, MultipartFile file);

    void deleteReview(Long idReview);
}
