package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {
    private String message;
    private String errorCode;
    private Object details;

    public MessageResponse(String message) {
        this.message = message;
    }

    public MessageResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
