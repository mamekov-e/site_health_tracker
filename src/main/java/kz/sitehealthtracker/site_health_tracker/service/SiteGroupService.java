package kz.sitehealthtracker.site_health_tracker.service;

import kz.sitehealthtracker.site_health_tracker.model.Site;
import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;

import java.util.List;
import java.util.Set;

public interface SiteGroupService {
    List<SiteGroup> getAllSiteGroups();

    Set<Site> getAllGroupSitesById(Long id);

    SiteGroup getSiteGroupById(Long id);

    void addSiteGroup(SiteGroup siteGroup);

    void addSitesToGroupById(Set<Site> siteList, Long id);

    SiteGroup updateSiteGroup(SiteGroup siteGroup);

    void deleteSiteGroupById(Long id);

    void deleteSitesFromGroupById(List<Site> siteList, Long id);

}
