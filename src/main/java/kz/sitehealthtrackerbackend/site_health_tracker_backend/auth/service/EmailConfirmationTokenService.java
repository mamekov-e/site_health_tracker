package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.EmailConfirmationToken;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.User;

import java.time.LocalDateTime;

public interface EmailConfirmationTokenService {
    EmailConfirmationToken findByToken(String token);

    EmailConfirmationToken save(EmailConfirmationToken emailConfirmation);

    EmailConfirmationToken createEmailConfirmationToken(User savedUser, LocalDateTime expirationDateTime);

    EmailConfirmationToken validateEmailConfirmation(String confirmationToken);

    User resetUserToken(String confirmationToken);
}
