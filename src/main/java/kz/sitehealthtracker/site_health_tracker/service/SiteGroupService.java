package kz.sitehealthtracker.site_health_tracker.service;

import kz.sitehealthtracker.site_health_tracker.model.Site;
import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;

import java.util.List;

public interface SiteGroupService {
    List<SiteGroup> getAllSiteGroups();

    List<SiteGroup> getAllSiteGroupsBySite(Site site);

    List<Site> getAllGroupSitesById(Long id);

    SiteGroup getSiteGroupById(Long id);

    boolean addSiteGroup(SiteGroup siteGroup);

    boolean addSitesToGroupById(List<Site> sitesOfGroup, Long id);

    SiteGroup updateSiteGroup(SiteGroup siteGroup);

    boolean updateGroupStatus(SiteGroup siteGroup);

    void saveGroupChangesIfGroupStatusWasNotChanged(SiteGroup siteGroup);

    boolean deleteSiteGroupById(Long id);

    boolean deleteSitesFromGroupById(List<Site> sitesOfGroup, Long id);

}
