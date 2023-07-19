package kz.sitehealthtracker.site_health_tracker.service;

import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;

public interface TelegramBotNotifierService {
    void notifyTelegramUsers(SiteGroup siteGroup);
}
