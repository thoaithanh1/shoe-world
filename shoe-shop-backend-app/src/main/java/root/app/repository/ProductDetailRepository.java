package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import root.app.dto.SizeShowClientDto;
import root.app.entity.ProductDetail;

import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

    @Query("select pd from ProductDetail pd where pd.product.id = :productId and pd.size.id = :sizeId")
    ProductDetail findProductDetailByProductAndSize(@Param("productId") Long productId, @Param("sizeId") Long sizeId);

    @Modifying
    @Transactional
    @Query("update ProductDetail pd set pd.quantity = :qty where pd.id = :productDetailId")
    int updateQtyProduct(@Param("productDetailId") Long productDetailId, @Param("qty") Integer qty);
}
