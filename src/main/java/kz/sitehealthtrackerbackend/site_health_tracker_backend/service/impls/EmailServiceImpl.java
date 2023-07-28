package kz.sitehealthtrackerbackend.site_health_tracker_backend.service.impls;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.BadRequestException;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.NotFoundException;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.EntityNames;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Email;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.repository.EmailRepository;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.EmailService;
import org.apache.commons.lang3.RandomUtils;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.CacheConstants.ENABLED_EMAILS_CACHE_NAME;
import static kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.ExpireConstants.CODE_EXPIRATION_TIME;
import static kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.ExpireConstants.RANDOM_VERIFICATION_CODE_LENGTH;

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
            throw BadRequestException.entityWithFieldValueAlreadyExist(EntityNames.EMAIL.getName(), address);
        }

        String randomCode = RandomString.make(RANDOM_VERIFICATION_CODE_LENGTH);
        email.setAddress(address);
        email.setVerificationCode(randomCode);
        LocalDateTime codeExpirationTime = LocalDateTime.now().plus(CODE_EXPIRATION_TIME);
        email.setCodeExpirationTime(codeExpirationTime);

        emailRepository.save(email);
        System.out.println("added:" + email);
        return email;
    }

    @CacheEvict(value = ENABLED_EMAILS_CACHE_NAME, allEntries = true, condition = "#result == true")
    @Override
    public boolean unregisterEmailFromNotifier(String address) {
        Email email = emailRepository.findByAddress(address)
                .orElseThrow(() -> NotFoundException.entityNotFoundBy(EntityNames.EMAIL.getName(), address));

        emailRepository.delete(email);
        return true;
    }

    @Override
    public void deleteAllByEnabledFalseAndCodeExpirationTimeBefore(LocalDateTime currentTime) {
        emailRepository.deleteAllByEnabledFalseAndCodeExpirationTimeBefore(currentTime);
    }

}
