package kz.sitehealthtracker.site_health_tracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "telegram_users")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class TelegramUser {
    @Id
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "enabled")
    private boolean enabled;
}
