package root.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private Integer rating;

    private String comment;

    @Column(name = "review_time")
    private Date reviewTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @PrePersist
    private void prePersist() {
        this.reviewTime = new Date();
    }
}
