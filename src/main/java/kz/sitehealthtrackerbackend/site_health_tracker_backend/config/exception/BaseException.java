package kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BaseException extends RuntimeException {
    private final Integer status;
    private final String description;

    private final Object body;

    public BaseException(HttpStatus httpStatus, String description) {
        super(httpStatus.getReasonPhrase());
        this.status = httpStatus.value();
        this.description = description;
        body = null;
    }

    public BaseException(HttpStatus httpStatus, String description, Object body) {
        super(httpStatus.getReasonPhrase());
        this.status = httpStatus.value();
        this.description = description;
        this.body = body;
    }
}
