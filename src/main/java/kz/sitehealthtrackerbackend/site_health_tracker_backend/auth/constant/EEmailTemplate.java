package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.constant;

import lombok.Getter;

@Getter
public enum EEmailTemplate {
    EMAIL_CONFIRMATION("email/email-confirmation.html"),
    RESET_PASSWORD("email/email-reset-password.html"),
    USER_NEW_PASSWORD("email/email-user-new-password.html");

    private final String name;

    EEmailTemplate(String name) {
        this.name = name;
    }
}
