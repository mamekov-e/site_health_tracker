package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.EmailConfirmationToken;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.dto.Mail;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.PasswordResetToken;

public interface EmailSenderService {
    void send(Mail mail, String templateName);

    void sendEmailConfirmation(String email, EmailConfirmationToken token);

    void sendEmailResetPassword(String email, PasswordResetToken token);

    void sendEmailUserNewPassword(String email, String password);

}
