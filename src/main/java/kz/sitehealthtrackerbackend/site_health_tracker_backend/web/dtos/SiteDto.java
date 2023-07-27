package kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteDto {
    private Long id;
    private String name;
    private String description;
    private String url;
    private Long siteHealthCheckInterval;
    private SiteStatus status;

    public SiteDto(String name, SiteStatus status) {
        this.name = name;
        this.status = status;
    }
}
