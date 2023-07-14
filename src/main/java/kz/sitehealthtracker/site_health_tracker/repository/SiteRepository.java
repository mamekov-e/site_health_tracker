package kz.sitehealthtracker.site_health_tracker.repository;

import kz.sitehealthtracker.site_health_tracker.model.Site;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SiteRepository extends JpaRepository<Site, Long> {
    boolean existsSitesByUrlIsIgnoreCase(String url);

    boolean existsSitesByUrlAndIdIsNot(String url, Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update Site set name = :siteName,description = :siteDescription,url = :siteUrl where id = :siteId")
    void updateSiteById(@Param("siteName") String siteName,
                        @Param("siteDescription") String siteDescription,
                        @Param("siteUrl") String siteUrl,
                        @Param("siteId") Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update Site set status = :siteStatus where id = :siteId")
    void updateSiteStatusById(@Param("siteStatus") SiteStatus status, @Param("siteId") Long id);
}
