package root.app.entity;

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
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "created_by")
    private Date createdBy;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @PrePersist
    public void prePersist() {
        this.createdDate = new Date();
    }
}
