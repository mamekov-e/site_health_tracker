package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.enums.ERole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users", schema = "auth",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(max = 50)
    private String middleName;

    private String phone;

    private boolean emailConfirmed;

    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", schema = "auth",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(Long id) {
        this.id = id;
    }

    public User(String email,
                String password,
                String firstName,
                String lastName,
                String middleName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public ERole getRole() {
        return !this.getRoles().isEmpty() ? this.getRoles().iterator().next().getName() : null;
    }

}
