package kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception;

import org.springframework.http.HttpStatus;

public class BadCredentialException extends BaseException {

    public BadCredentialException(String description) {
        super(HttpStatus.UNAUTHORIZED, description);
    }
}
