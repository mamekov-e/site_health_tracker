package kz.sitehealthtracker.site_health_tracker.service;

import kz.sitehealthtracker.site_health_tracker.model.Email;

public interface EmailNotifierService {
    boolean verify(String code);

    void sendNotification(String to);

    Email registerEmailToNotifier(String address, String urlPath);

    void unregisterEmailFromNotifier(String address);
}
