package kz.sitehealthtracker.site_health_tracker.web.dtos;

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
    private String status;
}
