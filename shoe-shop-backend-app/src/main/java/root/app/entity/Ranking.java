package root.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "rankings")
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "rank_conditions")
    private BigDecimal condition;

    @Column(name = "rank_discount")
    private BigDecimal discount;

    @OneToMany(mappedBy = "ranking")
    private List<User> users;
}
