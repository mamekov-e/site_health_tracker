package kz.sitehealthtracker.site_health_tracker.repository;

import kz.sitehealthtracker.site_health_tracker.model.Site;
import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SiteGroupRepository extends JpaRepository<SiteGroup, Long> {

    List<SiteGroup> findAllBySitesIn(List<Site> sites);

    boolean existsSiteGroupsByNameIsIgnoreCase(String name);

    boolean existsSiteGroupsByNameAndIdIsNot(String name, Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update SiteGroup set name = :siteGroupName,description = :siteGroupDescription where id = :siteGroupId")
    void updateSiteGroupById(@Param("siteGroupName") String siteName,
                             @Param("siteGroupDescription") String siteDescription,
                             @Param("siteGroupId") Long siteGroupId);
}
