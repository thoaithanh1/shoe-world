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
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer quantity;

    private BigDecimal price;

    private String size;

    private String color;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderDetail(String name, Integer quantity, BigDecimal price, String size, String color, ProductDetail productDetail, Order order) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.size = size;
        this.color = color;
        this.productDetail = productDetail;
        this.order = order;
    }
}
