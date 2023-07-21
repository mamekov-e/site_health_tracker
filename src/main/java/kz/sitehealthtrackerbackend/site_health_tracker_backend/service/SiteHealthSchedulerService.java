package kz.sitehealthtrackerbackend.site_health_tracker_backend.service;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;

public interface SiteHealthSchedulerService {
    void checkSiteHealth(Site site);

    void addScheduledTask(Site site);

    void updateScheduledTask(Site oldSite, Site site);

    void deleteScheduledTask(Site site);
}
