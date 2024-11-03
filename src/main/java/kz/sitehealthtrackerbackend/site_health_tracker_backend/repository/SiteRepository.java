package kz.sitehealthtrackerbackend.site_health_tracker_backend.repository;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site, Long> {
    boolean existsSitesByNameIgnoreCaseAndIdIsNotAndUser_IdIs(String name, Long id, Long userId);

    boolean existsSitesByNameIgnoreCaseAndUser_IdIs(String name, Long userId);

    boolean existsSitesByUrlIgnoreCaseAndUser_IdIs(String url, Long userId);

    boolean existsSitesByUrlIgnoreCaseAndIdIsNotAndUser_IdIs(String url, Long id, Long userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "update Site set status = :siteStatus where id = :siteId")
    void updateSiteStatusById(@Param("siteStatus") SiteStatus status, @Param("siteId") Long id);

    @Query(value = "from Site s where s.user.id = :userId and lower(concat(s.name,s.description,s.url,s.siteHealthCheckInterval)) " +
            "LIKE concat('%', lower(:searchText), '%') order by s.name asc")
    Page<Site> findAllInPageWithSearchText(@Param("searchText") String searchText, Long userId, Pageable pageable);

    @Query(value = "select s.* from sht.sites s " +
            "inner join sht.group_site gs on gs.site_id=s.id " +
            "inner join sht.site_groups sg on gs.group_id=sg.id " +
            "where s.user_id = :userId and lower(concat(s.name,s.description,s.url,s.site_health_check_interval)) " +
            "LIKE concat('%', lower(:searchText), '%')  and sg.id =:siteGroupId order by s.name",
            countQuery = "select count(s.*) from sites s " +
                    "inner join sht.group_site gs on gs.site_id=s.id " +
                    "inner join sht.site_groups sg on gs.group_id=sg.id " +
                    "where lower(concat(s.name,s.description,s.url,s.site_health_check_interval)) " +
                    "LIKE concat('%', lower(:searchText), '%')  and sg.id =:siteGroupId",
            nativeQuery = true)
    Page<Site> findAllGroupSitesInPageWithSearchText(@Param("siteGroupId") Long siteGroupId,
                                                     @Param("searchText") String searchText,
                                                     @Param("userId") Long userId,
                                                     Pageable pageable);

    List<Site> findAllByGroupsIn(List<SiteGroup> siteGroupsList);

    Page<Site> findAllByUser_Id(Long userId, Pageable pageable);

    List<Site> findAllByUser_Id(Long userId);
}
