package kz.sitehealthtrackerbackend.site_health_tracker_backend.service;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteStatus;

import java.util.List;

public interface SiteService {
    List<Site> getAllSites();

    boolean deleteSiteById(Long id);

    Site getSiteById(Long id);

    Site updateSite(Site site);

    Site updateSiteStatusById(SiteStatus status, Long id);

    void addSite(Site site);
}
