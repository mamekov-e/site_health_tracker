package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.annotation.ValidEmail;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SigninRequest {

    @NotBlank
    @ValidEmail
    private String email;

    @NotBlank
    private String password;
}

