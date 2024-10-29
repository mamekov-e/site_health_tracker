package kz.sitehealthtrackerbackend.site_health_tracker_backend.web;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.Configuration;

@SecurityRequirement(name = "bearerAuth")
@Configuration
public interface SecurityController {
}
