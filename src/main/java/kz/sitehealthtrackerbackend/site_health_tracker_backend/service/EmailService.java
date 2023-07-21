package kz.sitehealthtrackerbackend.site_health_tracker_backend.service;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Email;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailService {
    boolean verify(String code);

    Email registerEmailToNotifier(String address);

    boolean unregisterEmailFromNotifier(String address);

    void deleteAllByEnabledFalseAndCodeExpirationTimeBefore(LocalDateTime currentTime);

    List<Email> findAllByEnabledTrue();
}
