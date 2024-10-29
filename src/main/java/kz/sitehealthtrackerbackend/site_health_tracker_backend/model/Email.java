package kz.sitehealthtrackerbackend.site_health_tracker_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "emails", schema = "sht")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Email email = (Email) o;
        return isEnabled() == email.isEnabled() && Objects.equals(getAddress(), email.getAddress()) && Objects.equals(getVerificationCode(), email.getVerificationCode()) && Objects.equals(getCodeExpirationTime(), email.getCodeExpirationTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAddress(), getVerificationCode(), getCodeExpirationTime(), isEnabled());
    }
}
