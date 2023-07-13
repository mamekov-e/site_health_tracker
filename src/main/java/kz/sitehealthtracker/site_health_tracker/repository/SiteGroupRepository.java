package kz.sitehealthtracker.site_health_tracker.repository;

import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteGroupRepository extends JpaRepository<SiteGroup, Long> {

    boolean existsSiteGroupsByNameIsIgnoreCase(String name);

    boolean existsSiteGroupsByNameAndIdIsNot(String name, Long id);
}
