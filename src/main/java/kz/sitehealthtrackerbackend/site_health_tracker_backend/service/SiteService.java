package kz.sitehealthtrackerbackend.site_health_tracker_backend.service;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SiteService {
    Page<Site> getAllSiteInPageWithSearchText(Pageable pageable, String searchText);

    Page<Site> getAllGroupSitesInPageWithSearchText(Long siteGroupId, Pageable pageable, String searchText);

    Page<Site> getAllSiteInPage(Pageable pageable);

    List<Site> getAllSites();

    boolean deleteSiteById(Long id);

    Site getSiteById(Long id);

    Site updateSite(Site site);

    Site updateSiteStatusById(SiteStatus status, Long id);

    void addSite(Site site);
}
