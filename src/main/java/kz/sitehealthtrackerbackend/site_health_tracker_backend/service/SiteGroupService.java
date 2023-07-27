package kz.sitehealthtrackerbackend.site_health_tracker_backend.service;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SiteGroupService {
    Page<SiteGroup> getAllSiteGroupsInPageWithSearchText(Pageable pageable, String searchText);

    Page<SiteGroup> getAllSiteGroupsInPage(Pageable pageable);

    List<SiteGroup> getAllSiteGroupsBySite(Site site);

    List<Site> getAllGroupSitesById(Long id);

    Page<Site> getAllSitesOfGroupByIdInPageWithSearchText(Long id, Pageable pageable, String searchText);

    SiteGroup getSiteGroupById(Long id);

    boolean addSiteGroup(SiteGroup siteGroup);

    boolean addSitesToGroupById(List<Site> sitesOfGroup, Long id);

    SiteGroup updateSiteGroup(SiteGroup siteGroup);

    boolean updateGroupStatus(SiteGroup siteGroup, SiteDto siteWithChangedStatus);

    void saveGroupChangesIfGroupStatusWasNotChanged(SiteGroup siteGroup, SiteDto siteWithChangedStatus);

    boolean deleteSiteGroupById(Long id);

    boolean deleteSitesFromGroupById(List<Site> sitesOfGroup, Long id);

}
