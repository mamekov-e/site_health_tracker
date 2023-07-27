package kz.sitehealthtrackerbackend.site_health_tracker_backend.notifier;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;

public interface EventListener {
    void update(SiteGroup siteGroup, Site siteWithChangedStatus);
}
