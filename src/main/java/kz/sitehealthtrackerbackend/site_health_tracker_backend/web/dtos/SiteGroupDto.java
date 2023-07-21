package kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteGroupStatus;
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
