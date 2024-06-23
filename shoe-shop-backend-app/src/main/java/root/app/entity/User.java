package root.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên không được trống")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Họ không được trống")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "Email không được trống")
    @Email(message = "Email không đúng định dạng", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Mật khẩu không được trống")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    private Integer gender;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "date_of_birth")
    private Date dob;

    private String avatar;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    private Boolean status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "ranking_id")
    @JsonIgnore
    private Ranking ranking;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Favorite> favorites;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @PrePersist
    private void prePersist() {
        this.createdDate = new Date();
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedDate = new Date();
    }
}
