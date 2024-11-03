package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request.ForgotPasswordRequest;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request.RefreshRequest;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request.RegisterUserRequest;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request.SigninRequest;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.response.JwtResponse;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.AuthService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.UserService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/public")
@RequiredArgsConstructor
public class PublicController {
    private final AuthService authService;
    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/signup")
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterUserRequest request) {
        userService.register(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping(value = "/confirm-email", produces = "text/plan;charset=utf-8")
    @Operation(summary = "Токен передается из URL (в параметре) при нажатии на кнопку в email-сообщении для подтверждения аккаунта")
    public String confirmEmail(@RequestParam String token) {
        authService.confirmEmail(token);
        return "Аккаунт успешно подтвержден.";
    }

    @PostMapping("/signin")
    @Operation(summary = "Вход в аккаунт")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody SigninRequest signInRequest) {
        return ResponseEntity.ok(authService.authenticateUser(signInRequest));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Перевыпуск access token-а по refresh token-у")
    public ResponseEntity<JwtResponse> refresh(@Valid @RequestBody RefreshRequest refreshRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshRequest));
    }

    @GetMapping(value = "/reset-password", produces = "text/plan;charset=utf-8")
    @Operation(summary = "Токен передается из URL (в параметре) при нажатии на кнопку в email-сообщении при сбросе пароля")
    public String resetUserPassword(@RequestParam String token) {
        authService.resetUserPassword(token);
        return "Пароль успешно обновлен. Ожидайте письма на почту.";
    }

    @PutMapping("/forgot-password")
    @Operation(summary = "Функционал восстановления пароля. На указанный email придет сообщения для сброса пароля")
    public ResponseEntity<HttpStatus> processPasswordForgot(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        authService.processPasswordForgot(forgotPasswordRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping(value = "emails/register/verify", produces = "text/plan;charset=utf-8")
    public String verifyEmail(@RequestParam("code") String code) {
        boolean verified = emailService.verify(code);

        if (verified) {
            return "Подписка успешно оформлена.";
        }

        return "Ваша подписка уже оформлена.";
    }
}