package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.repository;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUser_Id(Long userId);
}
