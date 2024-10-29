package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "email_confirmation_token", schema = "auth")
public class EmailConfirmationToken implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expirationDate;
}
