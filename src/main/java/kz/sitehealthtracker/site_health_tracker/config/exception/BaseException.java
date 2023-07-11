package kz.sitehealthtracker.site_health_tracker.config.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BaseException extends RuntimeException {
    private final Integer status;
    private final String description;

    public BaseException(HttpStatus httpStatus, String description) {
        super(httpStatus.getReasonPhrase());
        this.status = httpStatus.value();
        this.description = description;
    }
}
