package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import root.app.entity.ProductDetail;
import root.app.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findReviewByProductDetail(ProductDetail productDetail);

    Integer countReviewByProductDetail(ProductDetail productDetail);

    Integer countReviewByRatingAndProductDetail(Integer rating, ProductDetail productDetail);

    @Query("select r from Review r where r.order.id = ?1 and r.productDetail.id = ?2 and r.user.id = ?3")
    List<Review> checkProductReviewed(Long orderId, Long productDetailId, Long userId);

    @Query("select sum(r.rating) / count(r.id) from Review r where r.productDetail.id = ?1")
    Double getAverageRating(Long idProductDetail);
}
