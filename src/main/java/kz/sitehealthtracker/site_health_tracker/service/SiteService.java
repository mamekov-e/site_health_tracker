package kz.sitehealthtracker.site_health_tracker.service;

import kz.sitehealthtracker.site_health_tracker.model.Site;

import java.util.List;

public interface SiteService {
    List<Site> getAllSites();

    boolean deleteSiteById(Long id);

    Site getSiteById(Long id);

    Site updateSite(Site dtoToSite);

    void addSite(Site dtoToSite);
}
