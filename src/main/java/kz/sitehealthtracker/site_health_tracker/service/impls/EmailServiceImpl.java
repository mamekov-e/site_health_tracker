package kz.sitehealthtracker.site_health_tracker.service.impls;

import kz.sitehealthtracker.site_health_tracker.config.exception.BadRequestException;
import kz.sitehealthtracker.site_health_tracker.config.exception.NotFoundException;
import kz.sitehealthtracker.site_health_tracker.model.Email;
import kz.sitehealthtracker.site_health_tracker.repository.EmailRepository;
import kz.sitehealthtracker.site_health_tracker.service.EmailService;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static kz.sitehealthtracker.site_health_tracker.constants.CacheConstants.ENABLED_EMAILS_CACHE_NAME;
import static kz.sitehealthtracker.site_health_tracker.constants.EmailConstants.CODE_EXPIRATION_TIME;
import static kz.sitehealthtracker.site_health_tracker.constants.EmailConstants.RANDOM__VERIFICATION_CODE_LENGTH;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Cacheable(cacheNames = ENABLED_EMAILS_CACHE_NAME, key = "true")
    @Override
    public List<Email> findAllByEnabledTrue() {
        return emailRepository.findAllByEnabledTrue();
    }

    @CacheEvict(value = ENABLED_EMAILS_CACHE_NAME, condition = "#result == true", allEntries = true)
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

    @CacheEvict(value = ENABLED_EMAILS_CACHE_NAME, allEntries = true, condition = "#result != null")
    @Override
    public Email registerEmailToNotifier(String address) {
        Email email = emailRepository.findByAddress(address).orElse(new Email());

        if (email.getId() != null && email.isEnabled()) {
            throw BadRequestException.entityWithFieldValueAlreadyExist(Email.class.getSimpleName(), address);
        }

        String randomCode = RandomString.make(RANDOM__VERIFICATION_CODE_LENGTH);
        email.setAddress(address);
        email.setVerificationCode(randomCode);
        email.setCodeExpirationTime(CODE_EXPIRATION_TIME);

        emailRepository.save(email);
        System.out.println("added:" + email);
        return email;
    }

    @CacheEvict(value = ENABLED_EMAILS_CACHE_NAME, allEntries = true, condition = "#result == true")
    @Override
    public boolean unregisterEmailFromNotifier(String address) {
        Email email = emailRepository.findByAddress(address)
                .orElseThrow(() -> NotFoundException.entityNotFoundBy(Email.class.getSimpleName(), address));

        emailRepository.delete(email);
        return true;
    }

    @CacheEvict(value = ENABLED_EMAILS_CACHE_NAME, allEntries = true)
    @Override
    public void deleteAllByEnabledFalseAndCodeExpirationTimeBefore(LocalDateTime currentTime) {
        emailRepository.deleteAllByEnabledFalseAndCodeExpirationTimeBefore(currentTime);
    }

}
