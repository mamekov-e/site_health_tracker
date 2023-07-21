package kz.sitehealthtrackerbackend.site_health_tracker_backend.repository;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    List<TelegramUser> findAllByEnabledIs(boolean enabled);

}
