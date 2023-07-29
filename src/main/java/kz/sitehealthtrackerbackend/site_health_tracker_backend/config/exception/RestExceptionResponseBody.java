package kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception;

import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@ToString
public class RestExceptionResponseBody {
    private ZonedDateTime zonedDateTime;
    private String error;
    private String message;

    private Object body;

    public RestExceptionResponseBody(BaseException exception) {
        this.zonedDateTime = ZonedDateTime.now();
        this.error = exception.getMessage();
        this.message = exception.getDescription();
        this.body = exception.getBody();
    }

    public RestExceptionResponseBody(RuntimeException exception) {
        this.zonedDateTime = ZonedDateTime.now();
        this.error = exception.getMessage();
    }
}
