package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import root.app.entity.ProductDetail;
import root.app.entity.Review;
import root.app.exception.ImageNotFoundException;
import root.app.exception.ObjectNotFoundException;
import root.app.repository.ProductDetailRepository;
import root.app.repository.ReviewRepository;
import root.app.service.ReviewService;
import root.app.util.FileUploadUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final String UPLOAD_DIR_REVIEW = "E:/Angular/shoe-shop-frontend-app/src/assets/image-data/review-image/";

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductDetailRepository detailRepository;

    @Override
    public List<Review> findReviewByProductDetail(Long productDetailId) {
        ProductDetail productDetail = detailRepository.findById(productDetailId)
                .orElse(null);
        return reviewRepository.findReviewByProductDetail(productDetail);
    }

    @Override
    public Integer countReviewByProductDetail(Long productDetailId) {
        ProductDetail productDetail = detailRepository.findById(productDetailId)
                .orElse(null);
        return reviewRepository.countReviewByProductDetail(productDetail);
    }

    @Override
    public Integer countReviewByRatingAndProductDetail(Integer rating, Long productDetailId) {
        ProductDetail productDetail = detailRepository.findById(productDetailId)
                .orElse(null);
        return reviewRepository.countReviewByRatingAndProductDetail(rating, productDetail);
    }

    @Override
    public Boolean checkProductReviewed(Long orderId, Long productDetailId, Long userId) {
        List<Review> reviews = reviewRepository.checkProductReviewed(orderId, productDetailId, userId);
        return reviews.size() > 0;
    }

    @Override
    public Double getAverageRating(Long idProductDetail) {
        return reviewRepository.getAverageRating(idProductDetail);
    }

    @Override
    public Review saveReview(Review review, MultipartFile file) {
        boolean isUpdating = review.getId() != null;
        if(isUpdating) {
            Review reviewDB = reviewRepository.findById(review.getId())
                    .orElseThrow(() -> new ObjectNotFoundException("Không tồn tại đánh giá có id " + review.getId()));
            review.setReviewTime(reviewDB.getReviewTime());
        }
        if(!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            review.setImage(fileName);
            review.setReviewTime(new Date());
            Review saveReview = reviewRepository.save(review);

            try {
                String uploadDir = UPLOAD_DIR_REVIEW + saveReview.getId();
                FileUploadUtil.cleanDir(uploadDir);
                FileUploadUtil.saveFile(uploadDir, fileName, file);
            } catch (IOException e) {
                throw new ImageNotFoundException("Không tìm thấy file");
            }
            return saveReview;
        } else {
            review.setImage(null);
            return reviewRepository.save(review);
        }
    }

    @Override
    public void deleteReview(Long idReview) {
        reviewRepository.deleteById(idReview);
    }
}
