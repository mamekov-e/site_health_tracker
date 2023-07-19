package kz.sitehealthtracker.site_health_tracker.notifier;

import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;

public interface EventListener {
    void update(SiteGroup siteGroup);
}
