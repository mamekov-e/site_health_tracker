package kz.sitehealthtrackerbackend.site_health_tracker_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Email extends BaseEntity<Long> {
    @Serial
    private static final long serialVersionUID = -1787133761590176647L;
    @Column(name = "address")
    private String address;
    @Column(name = "verification_code")
    private String verificationCode;
    @Column(name = "code_expiration_time")
    private LocalDateTime codeExpirationTime;
    @Column(name = "enabled")
    private boolean enabled = false;

}
