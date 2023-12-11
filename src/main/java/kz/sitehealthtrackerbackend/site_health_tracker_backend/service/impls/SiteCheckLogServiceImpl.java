package kz.sitehealthtrackerbackend.site_health_tracker_backend.service.impls;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteCheckLog;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.repository.SiteCheckLogRepository;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteCheckLogService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class SiteCheckLogServiceImpl implements SiteCheckLogService {

    @Autowired
    private SiteCheckLogRepository siteCheckLogRepository;

    @Autowired
    private SiteService siteService;

    @Override
    public List<SiteCheckLog> getAllSiteCheckLogsBySiteId(Long siteId, LocalDate date) {
        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(date, LocalTime.MAX);
        Site site = siteService.getSiteById(siteId);
        return siteCheckLogRepository.findAllBySiteAndCheckTimeBetweenOrderByCheckTime(site, startDateTime, endDateTime);
    }

    @Override
    @Transactional
    public void addSiteCheckLog(Site site) {
        SiteCheckLog siteCheckLog = new SiteCheckLog(LocalDateTime.now(), site.getStatus(), site);
        siteCheckLogRepository.save(siteCheckLog);
    }

    @Override
    @Transactional
    public void deleteSiteCheckLogBySiteId(Long siteId) {
        siteCheckLogRepository.deleteBySiteId(siteId);
    }

}
