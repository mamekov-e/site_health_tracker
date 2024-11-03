package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.impl;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.PasswordResetToken;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.User;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request.ForgotPasswordRequest;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request.RefreshRequest;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request.SigninRequest;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.response.JwtResponse;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.security.MyUserDetails;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.security.jwt.JwtUtils;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.*;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.util.SecurePasswordGenerator;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.BadCredentialException;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmailSenderService emailSenderService;
    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailConfirmationTokenService emailConfirmationTokenService;

    @Override
    public JwtResponse authenticateUser(SigninRequest signInRequest) {
        User user = userService.findByEmail(signInRequest.getEmail());
        if (!user.isEmailConfirmed())
            throw new BadRequestException("Аккаунт не подтвержден.");

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialException("Почта или пароль введены неверно.");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.generateAccessToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken((MyUserDetails) authentication.getPrincipal());
        MyUserDetails userPrincipal = (MyUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(
                accessToken,
                refreshToken,
                userPrincipal.getId(),
                userPrincipal.getEmail(),
                roles);
    }

    @Override
    public JwtResponse refreshToken(RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.getRefreshToken();

        if (jwtUtils.validateRefreshJWT(refreshToken)) {
            String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
            MyUserDetails userPrincipal = MyUserDetails.build(userService.findByEmail(username));
            String newAccessToken = jwtUtils.generateAccessToken(userPrincipal);
            String newRefreshToken = jwtUtils.generateRefreshToken(userPrincipal);
            List<String> roles = userPrincipal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return new JwtResponse(
                    newAccessToken,
                    newRefreshToken,
                    userPrincipal.getId(),
                    userPrincipal.getEmail(),
                    roles);
        }

        throw new AccessDeniedException("Refresh token is not valid");
    }

    @Override
    public void processPasswordForgot(ForgotPasswordRequest forgotPasswordRequest) {
        User user = userService.findByEmail(forgotPasswordRequest.getEmail());

        PasswordResetToken token = passwordResetTokenService.createPasswordResetToken(user, LocalDateTime.now().plusMinutes(10));

        emailSenderService.sendEmailResetPassword(user.getEmail(), token);
    }

    @Override
    public void resetUserPassword(String token) {
        String generatedPassword = SecurePasswordGenerator.generatePassword();
        User user = passwordResetTokenService.resetUserToken(token, generatedPassword);

        emailSenderService.sendEmailUserNewPassword(user.getEmail(), generatedPassword);
    }

    @Override
    public void confirmEmail(String token) {
        User user = emailConfirmationTokenService.resetUserToken(token);

        user.setEmailConfirmed(true);
        userService.save(user);
    }
}
