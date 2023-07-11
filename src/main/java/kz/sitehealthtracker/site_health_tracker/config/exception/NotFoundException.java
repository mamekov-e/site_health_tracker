package kz.sitehealthtracker.site_health_tracker.config.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {

    public NotFoundException(String description) {
        super(HttpStatus.NOT_FOUND, description);
    }

    public static NotFoundException entityNotFoundById(String entityName, Long id) {
        return new NotFoundException(String.format("entity-%s-not-found-by-id-%d", entityName, id));
    }
}
