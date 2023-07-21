package kz.sitehealthtrackerbackend.site_health_tracker_backend.service;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;

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
