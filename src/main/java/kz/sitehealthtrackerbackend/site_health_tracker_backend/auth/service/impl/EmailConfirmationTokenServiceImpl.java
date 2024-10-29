package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.impl;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.EmailConfirmationToken;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.User;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.repository.EmailConfirmationTokenRepository;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.EmailConfirmationTokenService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.BadRequestException;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.NotFoundException;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.EntityNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EmailConfirmationTokenServiceImpl implements EmailConfirmationTokenService {
    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    @Override
    public EmailConfirmationToken findByToken(String token) {
        return emailConfirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> NotFoundException.entityNotFoundBy(EntityNames.TOKEN.getName(), token));
    }

    @Override
    public EmailConfirmationToken save(EmailConfirmationToken passwordEmailConfirmation) {
        return emailConfirmationTokenRepository.save(passwordEmailConfirmation);
    }

    @Override
    public EmailConfirmationToken createEmailConfirmationToken(User user, LocalDateTime expirationDateTime) {
        EmailConfirmationToken token = emailConfirmationTokenRepository.findByUser_Id(user.getId())
                .orElse(new EmailConfirmationToken());
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpirationDate(expirationDateTime);
        token = save(token);

        return token;
    }

    @Override
    public EmailConfirmationToken validateEmailConfirmation(String confirmationToken) {
        EmailConfirmationToken emailConfirmationToken = findByToken(confirmationToken);

        if (emailConfirmationToken.getExpirationDate().isBefore(LocalDateTime.now()))
            throw new BadRequestException("Время действия токена истекло");

        return emailConfirmationToken;
    }

    @Override
    public User resetUserToken(String emailConfirmation) {
        EmailConfirmationToken token = validateEmailConfirmation(emailConfirmation);

        emailConfirmationTokenRepository.delete(token);

        return token.getUser();
    }
}

