package kz.sitehealthtracker.site_health_tracker.config.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

    public BadRequestException(String description) {
        super(HttpStatus.BAD_REQUEST, description);
    }

    public static BadRequestException entityAlreadyExist(String entityName, String fieldValue) {
        return new BadRequestException(String.format("entity-%s-with-`%s`-already-exist", entityName, fieldValue));
    }
}
