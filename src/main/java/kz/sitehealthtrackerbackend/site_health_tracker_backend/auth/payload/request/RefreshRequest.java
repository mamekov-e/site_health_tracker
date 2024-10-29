package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshRequest {
    @NotBlank
    private String refreshToken;
}

