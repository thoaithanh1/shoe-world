package root.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "product_detail")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "main_image")
    private String mainImage;

    private Integer quantity;

    private BigDecimal price;

    private Integer sold;

    private BigDecimal discount;

    private Integer gender;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    @JsonIgnore
    private Promotion promotion;

    @PrePersist
    private void prePersist() {
        this.createdDate = new Date();
        this.sold = 0;
        this.discount = BigDecimal.ZERO;
        this.status = true;
    }
}
