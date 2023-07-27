package kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SiteStatus {
    UP("Доступен"),
    DELETED_FROM_GROUP("Удален из группы"),
    DELETED("Удален"),
    ADDED_TO_GROUP("Добавился в группу"),
    DOWN("Недоступен");

    private final String statusValue;

}
