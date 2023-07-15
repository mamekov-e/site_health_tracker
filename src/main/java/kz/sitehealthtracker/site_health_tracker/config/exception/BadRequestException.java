package kz.sitehealthtracker.site_health_tracker.config.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

    public BadRequestException(String description) {
        super(HttpStatus.BAD_REQUEST, description);
    }

    public BadRequestException(String description, Object body) {
        super(HttpStatus.BAD_REQUEST, description, body);
    }

    public static BadRequestException entityWithFieldValueAlreadyExist(String entityName, String fieldValue) {
        return new BadRequestException(String.format("entity-%s-with-field-value-`%s`-already-exist", entityName, fieldValue));
    }

    public static BadRequestException entityCollectionWithElementsFailedByExistence(String entityName, Object body, boolean exist) {
        String existOrNotValue = exist ? "already" : "does-not";
        return new BadRequestException(String.format("entity-collection-%s-with-elements-%s-exist", entityName, existOrNotValue), body);
    }
}
