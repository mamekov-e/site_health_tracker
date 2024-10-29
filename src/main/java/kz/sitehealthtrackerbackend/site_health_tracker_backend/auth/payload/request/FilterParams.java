package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilterParams {
    private String status;
}

