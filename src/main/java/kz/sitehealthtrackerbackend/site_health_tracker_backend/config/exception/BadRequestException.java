package kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

    public BadRequestException(String description) {
        super(HttpStatus.BAD_REQUEST, description);
    }

    public BadRequestException(String description, Object body) {
        super(HttpStatus.BAD_REQUEST, description, body);
    }

    public static BadRequestException entityWithFieldValueAlreadyExist(String entityName, String fieldValue) {
        return new BadRequestException(String.format("%s со значением поля `%s` уже существует", entityName, fieldValue));
    }

    public static BadRequestException entityCollectionWithElementsFailedByExistence(String entityName, Object body, boolean exist) {
        String existOrNotValue = exist ? "уже" : "не";
        return new BadRequestException(String.format("%s со списком выбранных элементов %s существует", entityName, existOrNotValue), body);
    }

    public static BadRequestException sendingMessageToEmailAddressFailed(String address) {
        return new BadRequestException(String.format("Отправка сообщения по адресу %s не удалась", address));
    }
}
