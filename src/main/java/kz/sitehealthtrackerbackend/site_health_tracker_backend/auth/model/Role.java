package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model;

import jakarta.persistence.*;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.enums.ERole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "roles", schema = "auth")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

}
