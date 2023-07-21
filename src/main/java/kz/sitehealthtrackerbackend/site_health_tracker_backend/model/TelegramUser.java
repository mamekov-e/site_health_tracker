package kz.sitehealthtrackerbackend.site_health_tracker_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "telegram_users")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class TelegramUser implements Serializable {
    @Serial
    private static final long serialVersionUID = -4424021175421850963L;
    @Id
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "enabled")
    private boolean enabled;
}
