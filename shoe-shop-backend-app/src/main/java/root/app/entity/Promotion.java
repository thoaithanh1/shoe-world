package root.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private Integer quantity;

    @Column(name = "discount_method")
    private Integer discountMethod;

    @Column(name = "discount_type")
    private Integer discountType;

    @Column(name = "discount_value", precision = 20)
    private BigDecimal discountValue;

    @Column(name = "conditions_apply", precision = 20)
    private BigDecimal conditionsApply;

    @Column(name = "max_discount_amount")
    private BigDecimal maxDiscountAmount;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "created_by")
    private String createdBy;

    private String note;

    private Boolean status;

    @JsonIgnore
    @OneToMany(mappedBy = "promotion")
    private List<ProductDetail> productDetails;

    @PrePersist
    public void prePersist() {
        this.createdDate = new Date();
    }
}
