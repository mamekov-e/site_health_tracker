package kz.sitehealthtracker.site_health_tracker.config.exception.handler;

import kz.sitehealthtracker.site_health_tracker.config.exception.BaseException;
import kz.sitehealthtracker.site_health_tracker.config.exception.RestExceptionResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.Objects.isNull;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final int DEFAULT_STATUS = HttpStatus.INTERNAL_SERVER_ERROR.value();

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(final RuntimeException runtimeException) {
        return ResponseEntity
                .status(DEFAULT_STATUS)
                .body(new RestExceptionResponseBody(runtimeException));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleBaseException(final BaseException baseException) {
        return ResponseEntity
                .status(isNull(baseException.getStatus()) ? DEFAULT_STATUS : baseException.getStatus())
                .body(new RestExceptionResponseBody(baseException));
    }
}
