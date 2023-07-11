package kz.sitehealthtracker.site_health_tracker.config.exception;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class RestExceptionResponseBody {
    private ZonedDateTime zonedDateTime;
    private String error;
    private String message;

    public RestExceptionResponseBody(BaseException exception) {
        this.zonedDateTime = ZonedDateTime.now();
        this.error = exception.getMessage();
        this.message = exception.getDescription();
    }

    public RestExceptionResponseBody(RuntimeException exception) {
        this.zonedDateTime = ZonedDateTime.now();
        this.error = exception.getMessage();
    }
}
