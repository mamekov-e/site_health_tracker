package kz.sitehealthtracker.site_health_tracker.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SiteGroupStatus {
    ALL_UP("UP"),
    ALL_DOWN("DOWN"),
    PARTIAL_UP("PARTIALLY UP"),
    NO_SITES("NO SITES");

    public final String StatusValue;
}
