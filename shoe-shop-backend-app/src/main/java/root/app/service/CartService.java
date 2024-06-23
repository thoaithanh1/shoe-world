package root.app.service;

import root.app.dto.CartDto;
import root.app.entity.Cart;
import root.app.entity.CartDetail;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    Cart findCartByUser(Long userId);

    Integer countProductInCartDetail(Long userId);

    BigDecimal totalPriceInCart(Long userId);

    List<CartDetail> getListCartDB(Long userId);

    String addProductToCartDB(CartDto cartDto);

    String updateProductQtyInCartByUser(CartDto cartDto);

    int deleteProductInCart(Long productDetailId);
}
