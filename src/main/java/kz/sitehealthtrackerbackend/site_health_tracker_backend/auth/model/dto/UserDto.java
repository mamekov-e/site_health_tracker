package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private String fullNameWithInitials;
    private String phone;
    private Set<String> roles;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;
}
