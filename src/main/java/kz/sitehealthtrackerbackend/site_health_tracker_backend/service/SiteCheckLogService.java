package kz.sitehealthtrackerbackend.site_health_tracker_backend.service;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteCheckLog;

import java.time.LocalDate;
import java.util.List;

public interface SiteCheckLogService {
    List<SiteCheckLog> getAllSiteCheckLogsBySiteId(Long siteId, LocalDate date);

    void addSiteCheckLog(Site site);

    void deleteSiteCheckLogBySiteId(Long siteId);
}
