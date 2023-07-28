package kz.sitehealthtrackerbackend.site_health_tracker_backend.constants;

import java.time.Duration;

public class ExpireConstants {
    public static final int RANDOM_VERIFICATION_CODE_LENGTH = 32;
    public static final String CODE_EXPIRATION_CHECK_INTERVAL_CRON = "@hourly";
    public static final Duration CODE_EXPIRATION_TIME = Duration.ofHours(1);
    public static final String DELETE_UNSUBSCRIBED_TELEGRAM_USERS_CRON = "@midnight";
    public static final Duration TELEGRAM_UNSUBSCRIBED_EXPIRATION_TIME = Duration.ofDays(1);
}
