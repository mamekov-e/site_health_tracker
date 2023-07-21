package kz.sitehealthtracker.site_health_tracker.model.statuses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SiteGroupStatus {
    ALL_UP("Все сайты работают"),
    ALL_DOWN("Все сайты упали"),
    PARTIAL_UP("Есть упавшие сайты"),
    NO_SITES("Сайты удалены с группы");

    private final String statusValue;
}
