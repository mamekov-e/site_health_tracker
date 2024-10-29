package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.ws.rs.InternalServerErrorException;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.constant.EEmailTemplate;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.constant.Errors;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.EmailConfirmationToken;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.dto.Mail;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.PasswordResetToken;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.service.EmailSenderService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String userName;
    @Value("${app.base-url}")
    private String appBaseUrl;
    @Value("${server.servlet.context-path}")
    private String contextApi;

    @Override
    @Async
    public void send(Mail mail, String templateName) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Context context = new Context();
            context.setVariables(mail.getModel());
            String html = templateEngine.process(templateName, context);
            helper.setTo(mail.getTo());
            helper.setFrom(mail.getFrom());
            helper.setSubject(mail.getSubject());
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Sending email failed: {}", e.getMessage(), e);
            throw new BadRequestException(Errors.EMAIL_MESSAGE_SENDING_FAILED, e.getMessage());
        } catch (Exception e) {
            log.error("Internal error: {}", e.getMessage(), e);
            throw new InternalServerErrorException(Errors.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public void sendEmailConfirmation(String email, EmailConfirmationToken token) {
        Mail mail = new Mail();
        mail.setFrom(this.userName);
        mail.setTo(email);
        mail.setSubject("Подтверждение аккаунта");

        Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("email", email);
        mailModel.put("confirmationUrl", String.format("%s%s/v1/public/confirm-email?token=%s", appBaseUrl, contextApi, token.getToken()));
        mail.setModel(mailModel);
        send(mail, EEmailTemplate.EMAIL_CONFIRMATION.getName());
    }

    @Override
    public void sendEmailResetPassword(String email, PasswordResetToken token) {
        Mail mail = new Mail();
        mail.setFrom(this.userName);
        mail.setTo(email);
        mail.setSubject("Сброс пароля");

        Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("resetUrl", String.format("%s%s/v1/public/reset-password?token=%s", appBaseUrl, contextApi, token.getToken()));
        mail.setModel(mailModel);
        send(mail, EEmailTemplate.RESET_PASSWORD.getName());
    }

    @Override
    public void sendEmailUserNewPassword(String email, String password) {
        Mail mail = new Mail();
        mail.setFrom(this.userName);
        mail.setTo(email);
        mail.setSubject("Новый пароль для Site Health Checker");

        Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("password", password);
        mail.setModel(mailModel);
        send(mail, EEmailTemplate.USER_NEW_PASSWORD.getName());
    }
}
