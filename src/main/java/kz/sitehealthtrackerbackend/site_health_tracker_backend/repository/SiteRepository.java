package kz.sitehealthtrackerbackend.site_health_tracker_backend.repository;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SiteRepository extends JpaRepository<Site, Long> {
    boolean existsSitesByUrlIsIgnoreCase(String url);

    boolean existsSitesByUrlAndIdIsNot(String url, Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update Site set status = :siteStatus where id = :siteId")
    void updateSiteStatusById(@Param("siteStatus") SiteStatus status, @Param("siteId") Long id);
}
