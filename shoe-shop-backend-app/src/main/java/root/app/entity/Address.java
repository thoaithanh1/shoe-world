package root.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String country;

    @Column(name = "code_city")
    private Integer codeCity;

    private String city;

    @Column(name = "code_district")
    private Integer codeDistrict;

    private String district;

    @Column(name = "code_ward")
    private Integer codeWard;

    private String wards;

    private String location;

    @Column(name = "is_default")
    private Boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
