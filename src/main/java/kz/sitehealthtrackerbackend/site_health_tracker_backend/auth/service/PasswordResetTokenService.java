package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.PasswordResetToken;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.User;

import java.time.LocalDateTime;

public interface PasswordResetTokenService {
    PasswordResetToken findByToken(String token);

    PasswordResetToken save(PasswordResetToken passwordResetToken);

    PasswordResetToken createPasswordResetToken(User savedUser, LocalDateTime expirationDateTime);

    PasswordResetToken validateResetToken(String confirmationToken);

    User resetUserToken(String passwordResetToken, String generatedPassword);
}
