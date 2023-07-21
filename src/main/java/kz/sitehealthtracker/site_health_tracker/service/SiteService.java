package kz.sitehealthtracker.site_health_tracker.service;

import kz.sitehealthtracker.site_health_tracker.model.Site;
import kz.sitehealthtracker.site_health_tracker.model.statuses.SiteStatus;

import java.util.List;

public interface SiteService {
    List<Site> getAllSites();

    boolean deleteSiteById(Long id);

    Site getSiteById(Long id);

    Site updateSite(Site site);

    Site updateSiteStatusById(SiteStatus status, Long id);

    void addSite(Site site);
}
