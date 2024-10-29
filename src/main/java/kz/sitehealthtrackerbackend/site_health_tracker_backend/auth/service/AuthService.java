package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request.ForgotPasswordRequest;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request.RefreshRequest;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request.SigninRequest;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.response.JwtResponse;

public interface AuthService {
    JwtResponse authenticateUser(SigninRequest signInRequest);

    JwtResponse refreshToken(RefreshRequest refreshRequest);

    void processPasswordForgot(ForgotPasswordRequest forgotPasswordRequest);

    void resetUserPassword(String token);

    void confirmEmail(String token);
}
