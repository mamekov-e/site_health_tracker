package kz.sitehealthtracker.site_health_tracker.service;

import kz.sitehealthtracker.site_health_tracker.model.Site;

public interface SiteHealthSchedulerService {
    void checkSiteHealth(Site site);

    void addScheduledTask(Site site);

    void updateScheduledTask(Site oldSite, Site site);

    void deleteScheduledTask(Site site);
}
