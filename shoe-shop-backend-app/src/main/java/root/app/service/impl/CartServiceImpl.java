package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.app.dto.CartDto;
import root.app.entity.Cart;
import root.app.entity.CartDetail;
import root.app.entity.ProductDetail;
import root.app.repository.CartDetailRepository;
import root.app.repository.CartRepository;
import root.app.repository.ProductDetailRepository;
import root.app.service.CartService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Override
    public Cart findCartByUser(Long userId) {
        return cartRepository.findCartByUser(userId);
    }

    @Override
    public Integer countProductInCartDetail(Long userId) {
        return cartDetailRepository.countProductInCartDetail(userId);
    }

    @Override
    public BigDecimal totalPriceInCart(Long userId) {
        return cartRepository.totalPriceInCart(userId);
    }

    @Override
    public List<CartDetail> getListCartDB(Long userId) {
        return cartDetailRepository.findCartDetailByCart(userId);
    }

    @Override
    public String addProductToCartDB(CartDto cartDto) {
        List<CartDetail> cartDetails = cartDetailRepository.findAll();
        Cart cartDB = cartRepository.findCartByUser(cartDto.getUserId());
        ProductDetail productDetail = productDetailRepository.findById(cartDto.getProductDetailId()).get();
        Integer quantity = cartDto.getQuantity();
        BigDecimal productPrice = productDetail.getPrice();
        BigDecimal totalPrice = cartDto.getTotalPrice();

        for(CartDetail item: cartDetails) {
            if(item.getProductDetail().getId().equals(cartDto.getProductDetailId())) {
                CartDetail cartDetailDB = cartDetailRepository.findById(item.getId()).get();
                CartDetail cartDetail = new CartDetail(
                        cartDetailDB.getId(),
                        cartDetailDB.getQuantity() + quantity,
                        cartDetailDB.getPrice(),
                        cartDB,
                        productDetail);
                CartDetail saveCartDetail = cartDetailRepository.save(cartDetail);
                Cart cart = new Cart(cartDB.getId(), cartDB.getTotal().add(totalPrice), cartDB.getUser());
                cartRepository.save(cart);
                return saveCartDetail.getId() > 0 ? "Successfully":"Failed";
            }
        }

        CartDetail cartDetail = new CartDetail(quantity, productPrice, cartDB, productDetail);
        CartDetail saveCartDetail = cartDetailRepository.save(cartDetail);

        Cart cart = new Cart(cartDB.getId(), cartDB.getTotal().add(totalPrice), cartDB.getUser());
        cartRepository.save(cart);
        return saveCartDetail.getId() > 0 ? "Successfully":"Failed";
    }

    @Override
    public String updateProductQtyInCartByUser(CartDto cart) {
        Cart cartDB = cartRepository.findCartByUser(cart.getUserId());
        cartDB.setTotal(cart.getTotalPrice());
        System.out.println("Total Price In Cart: " + cart.getTotalPrice());
        cartRepository.save(cartDB);
        return cartDetailRepository.updateProductQtyInCartByUser(
                cart.getUserId(),
                cart.getProductDetailId(),
                cart.getQuantity()
        ) != 0 ? "Successfully":"Failed";
    }

    @Override
    public int deleteProductInCart(Long productDetailId) {
        CartDetail cartDetail = cartDetailRepository.findCartDetailByProduct(productDetailId);
        Cart cartDB = cartRepository.findById(cartDetail.getCart().getId()).get();

        Cart cart = new Cart(
                cartDB.getId(),
                cartDB.getTotal().subtract(cartDetail.getPrice().multiply(BigDecimal.valueOf(cartDetail.getQuantity()))),
                cartDB.getUser());
        cartRepository.save(cart);
        cartDetailRepository.deleteProductInCart(productDetailId);
        return 0;
    }
}
