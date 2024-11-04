package kz.sitehealthtrackerbackend.site_health_tracker_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class TelegramUser implements Serializable {
    @Serial
    private static final long serialVersionUID = -4424021175421850963L;

    @Id
    private Long id;

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
        TelegramUser that = (TelegramUser) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername());
    }
}
