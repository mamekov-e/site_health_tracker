package kz.sitehealthtracker.site_health_tracker.service.impls;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kz.sitehealthtracker.site_health_tracker.config.exception.BadRequestException;
import kz.sitehealthtracker.site_health_tracker.config.exception.NotFoundException;
import kz.sitehealthtracker.site_health_tracker.constants.Delimiters;
import kz.sitehealthtracker.site_health_tracker.constants.SendingMessageTemplates;
import kz.sitehealthtracker.site_health_tracker.model.Email;
import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteGroupStatus;
import kz.sitehealthtracker.site_health_tracker.repository.EmailRepository;
import kz.sitehealthtracker.site_health_tracker.service.EmailNotifierService;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static kz.sitehealthtracker.site_health_tracker.constants.RandomValues.RANDOM__VERIFICATION_CODE_LENGTH;
import static kz.sitehealthtracker.site_health_tracker.constants.SendingMessageTemplates.EMAIL_VERIFICATION_MESSAGE_CONTENT_TEMPLATE;

@Service
public class EmailNotifierServiceImpl implements EmailNotifierService {

    public static final int CODE_EXPIRATION_CHECK_INTERVAL = 60000 * 60; // checks each 1 hour
    public static final LocalDateTime CODE_EXPIRATION_TIME = LocalDateTime.now().plusHours(1);
    @Autowired
    private EmailRepository emailRepository;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private JavaMailSender mailSender;

    @Transactional
    @Scheduled(fixedRate = CODE_EXPIRATION_CHECK_INTERVAL)
    public void checkVerificationCodeExpired() {
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println(currentTime);
        emailRepository.deleteAllByEnabledFalseAndCodeExpirationTimeBefore(currentTime);
    }

    @Override
    public boolean verify(String code) {
        Email email = emailRepository.findByVerificationCode(code);

        if (email == null || email.isEnabled()) {
            return false;
        } else {
            email.setEnabled(true);
            email.setVerificationCode(null);
            email.setCodeExpirationTime(null);
            emailRepository.save(email);
            return true;
        }
    }

    @Override
    public Email registerEmailToNotifier(String address, String urlPath) {
        Email email = emailRepository.findByAddress(address).orElse(new Email());

        if (email.getId() != null && email.isEnabled()) {
            throw BadRequestException.entityWithFieldValueAlreadyExist(Email.class.getSimpleName(), address);
        }

        String randomCode = RandomString.make(RANDOM__VERIFICATION_CODE_LENGTH);
        email.setAddress(address);
        email.setVerificationCode(randomCode);
        email.setCodeExpirationTime(CODE_EXPIRATION_TIME);

        emailRepository.save(email);
        sendEmailVerification(email, urlPath);
        System.out.println("added:" + email);
        return email;
    }

    @Override
    public void unregisterEmailFromNotifier(String address) {
        Email email = emailRepository.findByAddress(address)
                .orElseThrow(() -> NotFoundException.entityNotFoundBy(Email.class.getSimpleName(), address));

        emailRepository.delete(email);
    }

    @Override
    public void notifySubscribers(SiteGroup siteGroup, SiteGroupStatus oldStatus) {
        List<Email> subscribersList = emailRepository.findAllByEnabledTrue();

        if (!subscribersList.isEmpty()) {
            String subject = "Статус групп изменился";
            String content = SendingMessageTemplates
                    .groupStatusChangedTemplateWithDelimiter(siteGroup, Delimiters.HTML_BR);

            for (Email email : subscribersList) {
                sendMimeMessageInHtml(email.getAddress(), subject, content);
            }
        }
    }

    private void sendEmailVerification(Email email, String urlPath) {
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
