package kz.sitehealthtracker.site_health_tracker.notifier.listeners;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kz.sitehealthtracker.site_health_tracker.config.exception.BadRequestException;
import kz.sitehealthtracker.site_health_tracker.constants.Delimiters;
import kz.sitehealthtracker.site_health_tracker.constants.SendingMessageTemplates;
import kz.sitehealthtracker.site_health_tracker.model.Email;
import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;
import kz.sitehealthtracker.site_health_tracker.notifier.EventListener;
import kz.sitehealthtracker.site_health_tracker.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static kz.sitehealthtracker.site_health_tracker.constants.EmailConstants.CODE_EXPIRATION_CHECK_INTERVAL;
import static kz.sitehealthtracker.site_health_tracker.constants.SendingMessageTemplates.EMAIL_VERIFICATION_MESSAGE_CONTENT_TEMPLATE;

@Component
public class EmailNotificationListener implements EventListener {

    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailService emailService;

    @Transactional
    @Scheduled(fixedRate = CODE_EXPIRATION_CHECK_INTERVAL)
    public void checkVerificationCodeExpired() {
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println(currentTime);
        emailService.deleteAllByEnabledFalseAndCodeExpirationTimeBefore(currentTime);
    }

    @Override
    public void update(SiteGroup siteGroup) {
        List<Email> subscribersList = emailService.findAllByEnabledTrue();

        if (!subscribersList.isEmpty()) {
            String subject = "Статус групп изменился";
            String content = SendingMessageTemplates
                    .groupStatusChangedTemplateWithDelimiter(siteGroup, Delimiters.HTML_BR);

            for (Email email : subscribersList) {
                sendMimeMessageInHtml(email.getAddress(), subject, content);
            }
        }
    }


    public void sendEmailVerification(Email email, String urlPath) {
        String subject = "Подтвердите вашу подписку";
        String verificationUrl = urlPath + "/verify?code=" + email.getVerificationCode();
        String content = String.format(EMAIL_VERIFICATION_MESSAGE_CONTENT_TEMPLATE, verificationUrl);

        sendMimeMessageInHtml(email.getAddress(), subject, content);
    }

    private void sendMimeMessageInHtml(String receiver, String subject, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

        try {
            messageHelper.setFrom(sender);
            messageHelper.setTo(receiver);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            throw BadRequestException.sendingMessageToEmailAddressFailed(receiver);
        }
    }
}
