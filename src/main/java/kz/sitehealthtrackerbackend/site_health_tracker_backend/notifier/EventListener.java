package kz.sitehealthtrackerbackend.site_health_tracker_backend.notifier;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteDto;

public interface EventListener {
    void update(SiteGroup siteGroup, SiteDto siteWithChangedStatus);
}
