package kz.sitehealthtracker.site_health_tracker.repository;

import kz.sitehealthtracker.site_health_tracker.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Long> {
    boolean existsSitesByUrlIsIgnoreCase(String url);

    boolean existsSitesByUrlAndIdIsNot(String url, Long id);
}
