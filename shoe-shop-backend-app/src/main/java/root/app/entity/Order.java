package root.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import root.app.contant.OrderContant;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Column(precision = 20)
    private BigDecimal total;

    @Column(name = "shipping_fee", precision = 20)
    private BigDecimal shippingFee;

    @Column(precision = 20)
    private BigDecimal discount;

    private Integer status;

    private String note;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "cancelled_date")
    private Date cancelledDate;

    @Column(name = "completed_date")
    private Date completedDate;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    public Order(String code, BigDecimal total, BigDecimal shippingFee, String note, PaymentMethod paymentMethod, User user, Address address, Promotion promotion, BigDecimal discount) {
        this.code = code;
        this.total = total;
        this.shippingFee = shippingFee;
        this.note = note;
        this.paymentMethod = paymentMethod;
        this.user = user;
        this.address = address;
        this.promotion = promotion;
        this.discount = discount;
    }

    @PrePersist
    private void prePersist() {
        this.createdDate = new Date();
        this.status = OrderContant.PENDING.value;
    }
}
