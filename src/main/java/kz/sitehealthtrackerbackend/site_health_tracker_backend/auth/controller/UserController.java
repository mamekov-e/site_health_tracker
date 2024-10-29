package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.User;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.dto.UserAdapter;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.UserService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.SecurityController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController implements SecurityController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Информация о текущем пользователе. Используется Bearer токена")
    public ResponseEntity<?> getCurrentUser() {
        User user = userService.getCurrentUser(true);
        return ResponseEntity.ok(UserAdapter.toDto(user));
    }

}
