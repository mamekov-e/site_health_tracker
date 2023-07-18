package kz.sitehealthtracker.site_health_tracker.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SiteGroupStatus {
    ALL_UP("Все сайты работают"),
    ALL_DOWN("Все сайты упали"),
    PARTIAL_UP("Есть упавшие сайты"),
    NO_SITES("Сайты удалены с группы");

    public final String STATUS_VALUE;
}
