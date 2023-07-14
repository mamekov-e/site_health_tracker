package kz.sitehealthtracker.site_health_tracker.scheduler;

import kz.sitehealthtracker.site_health_tracker.model.Site;
import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteStatus;
import kz.sitehealthtracker.site_health_tracker.service.SiteGroupService;
import kz.sitehealthtracker.site_health_tracker.service.SiteService;
import kz.sitehealthtracker.site_health_tracker.utils.HttpConnectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ScheduledTasks {
    private static final long SITE_HEALTH_CHECK_INTERVAL_IN_MILLIS = 20000; // 20sec

    @Autowired
    private SiteService siteService;

    @Autowired
    private SiteGroupService siteGroupService;

    @Scheduled(fixedRate = SITE_HEALTH_CHECK_INTERVAL_IN_MILLIS)
    public void checkSiteHealth() {
        List<Site> siteSet = siteService.getAllSites();
        List<Site> siteStatusChangedList = new ArrayList<>();

        for (Site site : siteSet) {
            String url = site.getUrl();
            HttpStatus status = HttpConnectionUtil.getUrlConnectionStatus(url);

            if (status.is2xxSuccessful() || status.is3xxRedirection()) {
                if (!site.getStatus().equals(SiteStatus.UP)) {
                    site.setStatus(SiteStatus.UP);
                    siteService.updateSiteStatusById(SiteStatus.UP, site.getId());
                    siteStatusChangedList.add(site);
                    System.out.printf("Status of site with url %s updated: %s%n", url, site.getStatus().name());
                }
            } else {
                if (!site.getStatus().equals(SiteStatus.DOWN)) {
                    site.setStatus(SiteStatus.DOWN);
                    siteService.updateSiteStatusById(SiteStatus.DOWN, site.getId());
                    siteStatusChangedList.add(site);
                    System.out.printf("Status of site with url %s updated: %s%n", url, site.getStatus().name());
                }
            }
        }

        if (!siteStatusChangedList.isEmpty()) {
            System.out.println("changed list: "+siteStatusChangedList);
            for (Site site : siteStatusChangedList) {
                List<SiteGroup> siteGroups = site.getGroups();
                System.out.println("groups list: "+siteGroups);
                if (!siteGroups.isEmpty()) {
                    for (SiteGroup siteGroup : siteGroups) {
                        siteGroupService.updateGroupStatus(siteGroup);
                        System.out.printf("Status of site group %s updated: %s%n", siteGroup.getName(), siteGroup.getStatus());
                    }
                }
            }
        } else {
            System.out.println("Site statuses were not changed");
        }

    }
}
