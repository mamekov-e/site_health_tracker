package kz.sitehealthtracker.site_health_tracker.service;

import kz.sitehealthtracker.site_health_tracker.model.Email;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailService {
    boolean verify(String code);

    Email registerEmailToNotifier(String address);

    void unregisterEmailFromNotifier(String address);

    void deleteAllByEnabledFalseAndCodeExpirationTimeBefore(LocalDateTime currentTime);

    List<Email> findAllByEnabledTrue();
}
