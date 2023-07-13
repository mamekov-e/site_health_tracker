package kz.sitehealthtracker.site_health_tracker.service;

import kz.sitehealthtracker.site_health_tracker.model.Site;
import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;

import java.util.List;

public interface SiteGroupService {
    List<SiteGroup> getAllSiteGroups();

    List<Site> getAllGroupSitesById(Long id);

    SiteGroup getSiteGroupById(Long id);

    void addSiteGroup(SiteGroup dtoToSite);

    void addSitesToGroup(List<Site> dtoToSiteList, Long groupId);

    SiteGroup updateSiteGroup(SiteGroup dtoToSite);

    void deleteSiteGroupById(Long id);

    void deleteSitesFromGroup(List<Site> dtoToSiteList, Long groupId);

}
