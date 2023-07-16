package kz.sitehealthtracker.site_health_tracker.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    private Long id;
    private String email;
    private String verificationCode;
    private boolean enabled = false;
}
