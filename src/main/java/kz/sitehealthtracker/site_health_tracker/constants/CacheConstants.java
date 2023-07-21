package kz.sitehealthtracker.site_health_tracker.constants;

import java.time.Duration;

public class CacheConstants {
    public static final Duration REDIS_CACHE_DURATION = Duration.ofMinutes(10);

    public static final String SITES_CACHE_NAME = "sites";
    public static final Duration SITES_CACHE_DURATION = Duration.ofMinutes(1);

    public static final String SITE_CACHE_NAME = "site";
    public static final Duration SITE_CACHE_DURATION = Duration.ofMinutes(1);

    public static final String SITE_GROUPS_CACHE_NAME = "site_groups";
    public static final Duration SITE_GROUPS_CACHE_DURATION = Duration.ofMinutes(5);

    public static final String SITE_GROUP_CACHE_NAME = "site_group";
    public static final Duration SITE_GROUP_CACHE_DURATION = Duration.ofMinutes(5);

    public static final String SITES_OF_GROUP_CACHE_NAME = "sites_of_group";
    public static final Duration SITES_OF_GROUP_CACHE_DURATION = Duration.ofMinutes(5);

    public static final String GROUPS_OF_SITE_CACHE_NAME = "groups_of_site";
    public static final Duration GROUPS_OF_SITE_CACHE_DURATION = Duration.ofMinutes(5);

    public static final String ENABLED_EMAILS_CACHE_NAME = "enabled_emails";
    public static final Duration ENABLED_EMAILS_CACHE_DURATION = Duration.ofMinutes(15);

    public static final String TELEGRAM_USER_CACHE_NAME = "telegram_user";
    public static final Duration TELEGRAM_USER_CACHE_DURATION = Duration.ofMinutes(15);

    public static final String ENABLED_TELEGRAM_USERS_CACHE_NAME = "enabled_telegram_users";
    public static final Duration ENABLED_TELEGRAM_USERS_CACHE_DURATION = Duration.ofMinutes(15);
}

