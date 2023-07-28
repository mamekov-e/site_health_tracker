package kz.sitehealthtrackerbackend.site_health_tracker_backend.repository;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SiteGroupRepository extends JpaRepository<SiteGroup, Long> {

    List<SiteGroup> findAllBySitesIn(List<Site> sites);

    @Query(value = "from SiteGroup sg where lower(concat(sg.name,sg.description)) " +
            "LIKE concat('%', lower(:searchText), '%') order by sg.name asc")
    Page<SiteGroup> findAllInPageWithSearchText(Pageable pageable, @Param("searchText") String searchText);

    boolean existsSiteGroupsByNameIgnoreCaseIs(String name);

    boolean existsSiteGroupsByNameIgnoreCaseAndIdIsNot(String name, Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update SiteGroup set name = :siteGroupName,description = :siteGroupDescription where id = :siteGroupId")
    void updateSiteGroupById(@Param("siteGroupName") String siteName,
                             @Param("siteGroupDescription") String siteDescription,
                             @Param("siteGroupId") Long siteGroupId);
}
