package kz.sitehealthtracker.site_health_tracker.service;

import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteGroupStatus;

public interface TelegramBotNotifierService {
    void notifyTelegramUsers(SiteGroup siteGroup, SiteGroupStatus oldStatus);
}
