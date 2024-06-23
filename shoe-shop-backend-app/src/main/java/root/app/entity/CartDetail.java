package root.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "cart_detail")
public class CartDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    public CartDetail(Integer quantity, BigDecimal price, Cart cart, ProductDetail productDetail) {
        this.quantity = quantity;
        this.price = price;
        this.cart = cart;
        this.productDetail = productDetail;
    }
}
