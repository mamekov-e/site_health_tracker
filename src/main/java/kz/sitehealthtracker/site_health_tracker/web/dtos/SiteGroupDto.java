package kz.sitehealthtracker.site_health_tracker.web.dtos;

import kz.sitehealthtracker.site_health_tracker.model.statuses.SiteGroupStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteGroupDto {
    private Long id;
    private String name;
    private String description;
    private SiteGroupStatus status;
}
