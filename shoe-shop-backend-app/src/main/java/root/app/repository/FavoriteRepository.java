package root.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import root.app.dto.ProductShowDto;
import root.app.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("""
            select new root.app.dto.ProductShowDto
            (
            min(pd.id), 
            concat('Giày ', p.model, ' ', p.name, ' Màu ', c.name), 
            pd.mainImage, 
            (select pdp.price from ProductDetail pdp where pdp.id = min(pd.id)), 
            (select pdp.discount from ProductDetail pdp where pdp.id = min(pd.id)), 
            (select pdp.createdDate from ProductDetail pdp where pdp.id = min(pd.id)),
            p.status
            ) 
            from Favorite f JOIN ProductDetail pd ON f.productDetail.id = pd.id 
                            JOIN Product p ON p.id = pd.product.id
                            JOIN Color c on pd.color.id = c.id
            where f.user.id = :userId
            group by concat('Giày ', p.model, ' ', p.name, ' Màu ', c.name), pd.mainImage, p.status
            """)
    Page<ProductShowDto> findPageFavoriteByUser(@Param("userId") Long userId, Pageable pageable);

    @Query("select count(f.id) from Favorite f where f.user.id = :userId")
    int countProductInFavoriteByUser(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Favorite f where f.user.id = :userId and f.productDetail.id = :productDetailId")
    int deleteProductInFavoriteByUser(@Param("userId") Long userId,
                                      @Param("productDetailId") Long productDetailId);
}
