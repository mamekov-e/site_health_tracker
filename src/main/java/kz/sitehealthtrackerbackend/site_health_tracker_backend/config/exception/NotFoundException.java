package kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {

    public NotFoundException(String description) {
        super(HttpStatus.NOT_FOUND, description);
    }

    public static NotFoundException entityNotFoundById(String entityName, Long id) {
        return new NotFoundException(String.format("%s с %d не найден", entityName, id));
    }
    public static NotFoundException entityNotFoundBy(String entityName, String fieldValue) {
        return new NotFoundException(String.format("%s со значением поля %s не найден", entityName, fieldValue));
    }
}
