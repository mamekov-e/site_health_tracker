package kz.sitehealthtrackerbackend.site_health_tracker_backend.service;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Email;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailService {

    List<Email> getAllByEnabledTrue();

    Email registerEmailToNotifier(String address);

    boolean verify(String code);

    boolean unregisterEmailFromNotifier(String address);

    void deleteAllByEnabledFalseAndCodeExpirationTimeBefore(LocalDateTime currentTime);

}
