package kz.sitehealthtrackerbackend.site_health_tracker_backend.service.impls;

import jakarta.annotation.PostConstruct;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteCheckLog;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteStatus;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteCheckLogService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteGroupService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteHealthSchedulerService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.SiteService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.utils.HttpConnectionUtil;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
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
        System.out.println("Getting all sites: " + siteList);
        Set<Site> sitesOfTasks = scheduledTasks.keySet();
        System.out.println("Getting all sitesOfTasks: " + sitesOfTasks);
        if (siteList.isEmpty()) {
            System.out.println("No sites in db");
            System.out.println("All sites in scheduledTasks: " + scheduledTasks);
        } else if (siteList.size() != scheduledTasks.size()) {
            for (Site site : siteList) {
                addScheduledTask(site);
            }
        } else {
            System.out.println("------------------All sites in scheduledTasks exist: " + siteList);
        }
    }

    @Override
    public void checkSiteHealth(Site site) {
        Site siteInDb = siteService.getSiteById(site.getId());
        String url = siteInDb.getUrl();
        HttpStatus status = HttpConnectionUtil.getUrlConnectionStatus(url);

        boolean siteStatusChanged = false;

        if (status.is2xxSuccessful() || status.is3xxRedirection()) {
            if (!siteInDb.getStatus().equals(SiteStatus.UP)) {
                siteService.updateSiteStatusById(SiteStatus.UP, siteInDb.getId());
                siteInDb.setStatus(SiteStatus.UP);
                siteStatusChanged = true;
                System.out.printf("Status of site with url %s updated: %s%n", url, siteInDb.getStatus().name());
            }
        } else {
            if (!siteInDb.getStatus().equals(SiteStatus.DOWN)) {
                siteService.updateSiteStatusById(SiteStatus.DOWN, siteInDb.getId());
                siteInDb.setStatus(SiteStatus.DOWN);
                siteStatusChanged = true;
                System.out.printf("Status of site with url %s updated: %s%n", url, siteInDb.getStatus().name());
            }
        }

        siteCheckLogService.addSiteCheckLog(siteInDb);

        if (siteStatusChanged) {
            List<SiteGroup> siteGroups = siteGroupService.getAllSiteGroupsBySite(siteInDb);
            System.out.println("site: " + siteInDb);
            System.out.println("site groups list: " + siteGroups);
            SiteDto siteWithChangedStatus = new SiteDto(siteInDb.getName(), siteInDb.getStatus());
            if (!siteGroups.isEmpty()) {
                for (SiteGroup siteGroup : siteGroups) {
                    siteGroupService.updateGroupStatus(siteGroup, siteWithChangedStatus);
                }
            }
        } else {
            System.out.println("Site status was not changed");
        }

    }

    public void addScheduledTask(Site site) {
        System.out.println("ADD----------------------------------");
        System.out.println("All ST keys: " + scheduledTasks.keySet());
        if (scheduledTasks.containsKey(site)) {
            System.out.println("Exists site: " + site);
            return;
        }

        System.out.println("--------Adding: " + site);
        Long siteInterval = site.getSiteHealthCheckInterval();
        ScheduledFuture<?> scheduledTask = taskScheduler.scheduleWithFixedDelay(
                () -> checkSiteHealth(site),
                new Date().toInstant(),
                Duration.ofSeconds(siteInterval));
        scheduledTasks.put(site, scheduledTask);
        System.out.println("-------Added: " + scheduledTasks.keySet());
    }

    public void updateScheduledTask(Site oldSite, Site site) {
        System.out.println("UPDATE----------------------------------");
        if (!Objects.equals(oldSite, site)) {
            deleteScheduledTask(oldSite);
            addScheduledTask(site);
            System.out.println("Updated ---------------------------");
        } else {
            System.out.println("Was not updated: equal objs ---------------------------");
        }
    }

    public void deleteScheduledTask(Site site) {
        System.out.println("DELETE----------------------------------");
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(site);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
        }
        scheduledTasks.remove(site);
    }
}
