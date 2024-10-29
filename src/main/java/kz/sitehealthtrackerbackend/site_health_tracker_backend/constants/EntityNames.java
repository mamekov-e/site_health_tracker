package kz.sitehealthtrackerbackend.site_health_tracker_backend.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityNames {
    SITE("Сайт"),
    SITE_GROUP("Группа сайта"),
    EMAIL("Почта"),
    USER("Пользователь"),
    ROLE("Роль"),
    TOKEN("Токен"),
    TELEGRAM_USER("Пользователь телеграмм");

    private final String name;
}
