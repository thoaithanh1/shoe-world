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
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price")
    private BigDecimal total;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        this.total = BigDecimal.ONE;
    }
}
