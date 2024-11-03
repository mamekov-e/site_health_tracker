package kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.constant.DateFormats;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteCheckLogDto {
    private Long id;
    @JsonFormat(pattern = DateFormats.DATE_TIME_FORMAT)
    private LocalDateTime checkTime;
    private SiteStatus status;
}
