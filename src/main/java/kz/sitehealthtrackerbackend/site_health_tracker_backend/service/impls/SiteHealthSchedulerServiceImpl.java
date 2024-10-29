package kz.sitehealthtrackerbackend.site_health_tracker_backend.service.impls;

import jakarta.annotation.PostConstruct;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteStatus;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteCheckLogService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteGroupService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteHealthSchedulerService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.utils.HttpConnectionUtil;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
public class SiteHealthSchedulerServiceImpl implements SiteHealthSchedulerService {

    private final Map<Site, ScheduledFuture<?>> scheduledTasks;
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    private SiteService siteService;

    @Autowired
    private SiteCheckLogService siteCheckLogService;
    @Autowired
    private SiteGroupService siteGroupService;

    public SiteHealthSchedulerServiceImpl() {
        this.scheduledTasks = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void initializeSitesHealthScheduler() {
        List<Site> siteList = siteService.getAllSites();
        if (siteList.isEmpty()) {
            log.info("Нет сайтов в базе");
        } else if (siteList.size() != scheduledTasks.size()) {
            for (Site site : siteList) {
                addScheduledTask(site);
            }
        }
    }

    @Override
    public void checkSiteHealth(Site site) {
        Site siteInDb = siteService.getSiteById(site.getId(), false);
        String url = siteInDb.getUrl();
        HttpStatus status = HttpConnectionUtil.getUrlConnectionStatus(url);

        boolean siteStatusChanged = false;

        if (status.is2xxSuccessful() || status.is3xxRedirection()) {
            if (!siteInDb.getStatus().equals(SiteStatus.UP)) {
                siteService.updateSiteStatusById(SiteStatus.UP, siteInDb.getId());
                siteInDb.setStatus(SiteStatus.UP);
                siteStatusChanged = true;
                log.info("Статус сайта с url {} обновился: {}", url, siteInDb.getStatus().name());
            }
        } else {
            if (!siteInDb.getStatus().equals(SiteStatus.DOWN)) {
                siteService.updateSiteStatusById(SiteStatus.DOWN, siteInDb.getId());
                siteInDb.setStatus(SiteStatus.DOWN);
                siteStatusChanged = true;
                log.info("Статус сайта с url {} обновился: {}", url, siteInDb.getStatus().name());
            }
        }

        siteCheckLogService.addSiteCheckLog(siteInDb);

        if (siteStatusChanged) {
            List<SiteGroup> siteGroups = siteGroupService.getAllSiteGroupsBySite(siteInDb);
            SiteDto siteWithChangedStatus = new SiteDto(siteInDb.getName(), siteInDb.getStatus());
            if (!siteGroups.isEmpty()) {
                for (SiteGroup siteGroup : siteGroups) {
                    siteGroupService.updateGroupStatus(siteGroup, siteWithChangedStatus);
                }
            }
        } else {
            log.info("Статус сайта с url {} не обновился", url);
        }

    }

    public void addScheduledTask(Site site) {
        if (scheduledTasks.containsKey(site)) {
            log.info("Сайт уже содержится в sheduledTask: {}", site);
            return;
        }

        Long siteInterval = site.getSiteHealthCheckInterval();
        ScheduledFuture<?> scheduledTask = taskScheduler.scheduleAtFixedRate(
                () -> checkSiteHealth(site),
                new Date().toInstant(),
                Duration.ofSeconds(siteInterval));
        scheduledTasks.put(site, scheduledTask);
    }

    public void updateScheduledTask(Site oldSite, Site site) {
        if (!Objects.equals(oldSite, site)) {
            deleteScheduledTask(oldSite);
            addScheduledTask(site);
        } else {
            log.info("Объекты равны - сайт из базы: {}, новый данные сайта: {}", oldSite, site);
        }
    }

    public void deleteScheduledTask(Site site) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(site);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
        }
        scheduledTasks.remove(site);
    }
}
