package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.app.dto.CartDto;
import root.app.entity.Cart;
import root.app.entity.CartDetail;
import root.app.service.impl.CartServiceImpl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    private CartServiceImpl cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable("userId") Long userId) {
        Cart cart = cartService.findCartByUser(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<Integer> countProductInCartByUserId(@PathVariable("userId") Long userId) {
        Integer countProduct = cartService.countProductInCartDetail(userId);
        return new ResponseEntity<>(countProduct, HttpStatus.OK);
    }

    @GetMapping("/total/{userId}")
    public ResponseEntity<BigDecimal> totalPriceInCartByUserId(@PathVariable("userId") Long userId) {
        BigDecimal totalPrice = cartService.totalPriceInCart(userId);
        return new ResponseEntity<>(totalPrice, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartDetail>> getListCartDB(@PathVariable("userId") Long userId) {
        List<CartDetail> cartDetails = cartService.getListCartDB(userId);
        return new ResponseEntity<>(cartDetails, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addProductToCartDB(@RequestBody CartDto cartDto) {
        Map<String, String> result = new HashMap<>();
        result.put("status", cartService.addProductToCartDB(cartDto));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/update-qty")
    public ResponseEntity<?> updateProductQtyInCartByUser(@RequestBody CartDto cart) {
        Map<String, String> result = new HashMap<>();
        result.put("status", cartService.updateProductQtyInCartByUser(cart));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{productDetailId}", method = RequestMethod.DELETE)
    public ResponseEntity<Integer> deleteProductInCart(@PathVariable("productDetailId") Long productDetailId) {
        int result = cartService.deleteProductInCart(productDetailId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
