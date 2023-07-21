package kz.sitehealthtrackerbackend.site_health_tracker_backend.constants;

import java.time.LocalDateTime;

public class EmailConstants {
    public static final int RANDOM__VERIFICATION_CODE_LENGTH = 32;
    public static final int CODE_EXPIRATION_CHECK_INTERVAL = 60000 * 60; // checks each 1 hour
    public static final LocalDateTime CODE_EXPIRATION_TIME = LocalDateTime.now().plusHours(1);
}
