package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.repository;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {
    Optional<EmailConfirmationToken> findByToken(String token);

    Optional<EmailConfirmationToken> findByUser_Id(Long userId);
}
