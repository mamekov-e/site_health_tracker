package kz.sitehealthtracker.site_health_tracker.service;

import kz.sitehealthtracker.site_health_tracker.model.Email;
import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;

public interface EmailNotifierService {
    boolean verify(String code);

    Email registerEmailToNotifier(String address, String urlPath);

    void unregisterEmailFromNotifier(String address);

    void notifySubscribers(SiteGroup siteGroup);
}
