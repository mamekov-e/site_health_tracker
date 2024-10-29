package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.impl;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.PasswordResetToken;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.User;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.repository.PasswordResetTokenRepository;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.PasswordResetTokenService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.UserService;
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
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserService userService;

    @Override
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> NotFoundException.entityNotFoundBy(EntityNames.TOKEN.getName(), token));
    }

    @Override
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public PasswordResetToken createPasswordResetToken(User user, LocalDateTime expirationDateTime) {
        PasswordResetToken token = passwordResetTokenRepository.findByUser_Id(user.getId())
                .orElse(new PasswordResetToken());
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpirationDate(expirationDateTime);
        token = save(token);

        return token;
    }

    @Override
    public PasswordResetToken validateResetToken(String confirmationToken) {
        PasswordResetToken passwordResetToken = findByToken(confirmationToken);

        if (passwordResetToken.getExpirationDate().isBefore(LocalDateTime.now()))
            throw new BadRequestException("Время действия токена истекло");

        return passwordResetToken;
    }

    @Override
    public User resetUserToken(String passwordResetToken, String generatedPassword) {
        PasswordResetToken token = validateResetToken(passwordResetToken);
        User user = token.getUser();

        user.setPassword(generatedPassword);
        userService.updatePassword(user);

        passwordResetTokenRepository.delete(token);

        return user;
    }
}

