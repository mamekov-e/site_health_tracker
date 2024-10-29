package kz.sitehealthtrackerbackend.site_health_tracker_backend.utils;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.security.SecurityUtils;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.BadRequestException;

public class EntityUtils {
    public static void checkUserAllowed(Long userId) {
        if (!SecurityUtils.getCurrentUserId().equals(userId)) {
            throw new BadRequestException("Эта запись не доступна для вас.");
        }
    }
}
