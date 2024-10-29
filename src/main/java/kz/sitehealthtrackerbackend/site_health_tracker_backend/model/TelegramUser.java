package kz.sitehealthtrackerbackend.site_health_tracker_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "telegram_users", schema = "sht")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class TelegramUser extends BaseEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = -4424021175421850963L;

    @Column(name = "username")
    private String username;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "disabled_expiration_time")
    private LocalDateTime disabledExpirationTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TelegramUser that = (TelegramUser) o;
        return isEnabled() == that.isEnabled() && Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getDisabledExpirationTime(), that.getDisabledExpirationTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUsername(), isEnabled(), getDisabledExpirationTime());
    }
}
