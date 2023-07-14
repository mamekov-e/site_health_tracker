package kz.sitehealthtracker.site_health_tracker.service;

import kz.sitehealthtracker.site_health_tracker.model.Site;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SiteService {
    List<Site> getAllSites();

    void deleteSiteById(Long id);

    Site getSiteById(Long id);

    Site updateSite(Site site);

    void updateSiteStatusById(SiteStatus status, Long id);

    void addSite(Site site);
}
