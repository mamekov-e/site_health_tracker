package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request;


import jakarta.validation.constraints.NotBlank;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.annotation.ValidEmail;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ForgotPasswordRequest {
    @ValidEmail
    @NotBlank
    private String email;
}
