package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long id;
    private String email;
    private List<String> roles;
    private String refreshToken;

    public JwtResponse(String accessToken, String refreshToken, Long id, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.refreshToken = refreshToken;
    }
}

