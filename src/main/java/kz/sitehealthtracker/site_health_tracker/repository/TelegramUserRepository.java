package kz.sitehealthtracker.site_health_tracker.repository;

import kz.sitehealthtracker.site_health_tracker.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    List<TelegramUser> findAllByEnabledIs(boolean enabled);

}
