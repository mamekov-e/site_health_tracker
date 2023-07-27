package kz.sitehealthtrackerbackend.site_health_tracker_backend.repository;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SiteRepository extends JpaRepository<Site, Long> {
    boolean existsSitesByNameIgnoreCaseAndIdIsNot(String name, Long id);
    boolean existsSitesByNameIgnoreCase(String name);
    boolean existsSitesByUrlIgnoreCase(String url);

    boolean existsSitesByUrlIgnoreCaseAndIdIsNot(String url, Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update Site set status = :siteStatus where id = :siteId")
    void updateSiteStatusById(@Param("siteStatus") SiteStatus status, @Param("siteId") Long id);

    @Query(value = "from Site s where lower(concat(s.name,s.description,s.url,s.siteHealthCheckInterval,s.status)) " +
            "LIKE concat('%', lower(:searchText), '%') order by s.name asc")
    Page<Site> findAllInPageWithSearchText(Pageable pageable, @Param("searchText") String searchText);

    @Query(value = "select s.* from sites s " +
            "inner join group_site gs on gs.site_id=s.id " +
            "inner join site_groups sg on gs.group_id=sg.id " +
            "where lower(concat(s.name,s.description,s.url,s.site_health_check_interval,s.status)) " +
            "LIKE concat('%', lower(:searchText), '%')  and sg.id =:siteGroupId order by s.name",
            countQuery = "select count(s.*) from sites s " +
                    "inner join group_site gs on gs.site_id=s.id " +
                    "inner join site_groups sg on gs.group_id=sg.id " +
                    "where lower(concat(s.name,s.description,s.url,s.site_health_check_interval,s.status)) " +
                    "LIKE concat('%', lower(:searchText), '%')  and sg.id =:siteGroupId",
            nativeQuery = true)
    Page<Site> findAllGroupSitesInPageWithSearchText(@Param("siteGroupId") Long siteGroupId,
                                                     Pageable pageable,
                                                     @Param("searchText") String searchText);

}
