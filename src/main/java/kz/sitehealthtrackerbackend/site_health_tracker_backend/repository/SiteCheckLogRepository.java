package kz.sitehealthtrackerbackend.site_health_tracker_backend.repository;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteCheckLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SiteCheckLogRepository extends JpaRepository<SiteCheckLog, Long> {
    List<SiteCheckLog> findAllBySiteAndCheckTimeBetweenOrderByCheckTime(Site site, LocalDateTime startDateTime, LocalDateTime endDateTime);

}
