package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.app.entity.Cart;

import java.math.BigDecimal;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c where c.user.id = :userId")
    Cart findCartByUser(@Param("userId") Long userId);

    @Query("select c.total from Cart c where c.user.id = :userId")
    BigDecimal totalPriceInCart(@Param("userId") Long userId);
}
