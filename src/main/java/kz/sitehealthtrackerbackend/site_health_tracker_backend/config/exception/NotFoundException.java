package kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {

    public NotFoundException(String description) {
        super(HttpStatus.NOT_FOUND, description);
    }

    public static NotFoundException entityNotFoundById(String entityName, Long id) {
        return new NotFoundException(String.format("entity-%s-not-found-by-id-%d", entityName, id));
    }
    public static NotFoundException entityNotFoundBy(String entityName, String fieldValue) {
        return new NotFoundException(String.format("entity-%s-not-found-by-field-value-%s", entityName, fieldValue));
    }

    public static NotFoundException emailVerificationCodeExpired(String fieldValue) {
        return new NotFoundException(String.format("verification-code-%s-expired", fieldValue));
    }
}
