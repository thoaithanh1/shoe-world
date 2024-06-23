package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import root.app.entity.CartDetail;

import java.math.BigDecimal;
import java.util.List;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

    @Query("""
            select cd from CartDetail cd join Cart c on cd.cart.id = c.id 
            where c.user.id = :userId
            order by cd.productDetail.product.name
            """)
    List<CartDetail> findCartDetailByCart(@Param("userId") Long userId);

    @Query("select count(cd.id) from CartDetail cd where cd.cart.user.id = :userId")
    Integer countProductInCartDetail(@Param("userId") Long userId);

    @Query("select cd from CartDetail cd where cd.productDetail.id = :productDetailId")
    CartDetail findCartDetailByProduct(@Param("productDetailId") Long productDetailId);

    @Transactional
    @Modifying
    @Query("""
            update CartDetail cd set cd.quantity = :quantity
            where cd.productDetail.id = :productDetailId and cd.cart.user.id = :userId
            """)
    int updateProductQtyInCartByUser(@Param("userId") Long userId,
                                     @Param("productDetailId") Long productDetailId,
                                     @Param("quantity") Integer quantity);

    @Transactional
    @Modifying
    @Query("delete from CartDetail cd where cd.productDetail.id = :productDetailId")
    int deleteProductInCart(@Param("productDetailId") Long productDetailId);

    @Transactional
    @Modifying
    @Query("delete from CartDetail cd where cd.cart.id = :cartId")
    int deleteAllProductByCart(@Param("cartId") Long cartId);
}
