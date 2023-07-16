package kz.sitehealthtracker.site_health_tracker.service.impls;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kz.sitehealthtracker.site_health_tracker.config.exception.BadRequestException;
import kz.sitehealthtracker.site_health_tracker.config.exception.NotFoundException;
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
import org.springframework.stereotype.Service;

import java.util.List;

import static kz.sitehealthtracker.site_health_tracker.constants.RandomValues.RANDOM__VERIFICATION_CODE_LENGTH;
import static kz.sitehealthtracker.site_health_tracker.constants.SendingMessageTemplates.GROUP_STATUS_CHANGED_NOTIFICATION_MESSAGE_CONTENT_TEMPLATE;
import static kz.sitehealthtracker.site_health_tracker.constants.SendingMessageTemplates.VERIFICATION_MESSAGE_CONTENT_TEMPLATE;

@Service
public class EmailNotifierImpl implements EmailNotifierService {

    @Autowired
    private EmailRepository emailRepository;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public boolean verify(String code) {
        Email email = emailRepository.findByVerificationCode(code);

        if (email == null || email.isEnabled()) {
            return false;
        } else {
            email.setEnabled(true);
            email.setVerificationCode(null);
            emailRepository.save(email);
            return true;
        }
    }

    @Override
    public Email registerEmailToNotifier(String address, String urlPath) {
        Email email = emailRepository.findByAddress(address)
                .orElse(new Email());

        if (email.getId() != null && email.isEnabled()) {
            throw BadRequestException
                    .entityWithFieldValueAlreadyExist(Email.class.getSimpleName(), address);
        }

        String randomCode = RandomString.make(RANDOM__VERIFICATION_CODE_LENGTH);
        email.setAddress(address);
        email.setVerificationCode(randomCode);

        emailRepository.save(email);
        sendEmailVerification(email, urlPath);
        return email;
    }

    @Override
    public void unregisterEmailFromNotifier(String address) {
        Email email = emailRepository.findByAddress(address)
                .orElseThrow(() -> NotFoundException
                        .entityNotFoundBy(Email.class.getSimpleName(), address));

        emailRepository.delete(email);
    }

    @Override
    public void notifySubscribers(SiteGroup siteGroup, SiteGroupStatus oldStatus) {
        List<Email> subscribersList = emailRepository.findAllByEnabledIs(true);

        if (!subscribersList.isEmpty()) {
            String subject = "Group status changed";
            String content = String.format(GROUP_STATUS_CHANGED_NOTIFICATION_MESSAGE_CONTENT_TEMPLATE,
                    siteGroup.getName(), siteGroup.getStatus().getStatusValue(), oldStatus.getStatusValue());

            for (Email email : subscribersList) {
                sendMimeMessageInHtml(email.getAddress(), subject, content);
            }
        }
    }

    private void sendEmailVerification(Email email, String urlPath) {
        String subject = "Verify your registration";
        String verificationUrl = urlPath + "/verify?code=" + email.getVerificationCode();
        String content = String.format(VERIFICATION_MESSAGE_CONTENT_TEMPLATE, verificationUrl);

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
        } catch (MessagingException e) {
            throw BadRequestException.sendingMessageToEmailAddressFailed(receiver);
        }

        mailSender.send(mimeMessage);
    }
}
