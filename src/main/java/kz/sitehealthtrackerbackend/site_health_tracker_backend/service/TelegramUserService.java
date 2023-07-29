package kz.sitehealthtrackerbackend.site_health_tracker_backend.service;


import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.TelegramUser;

import java.time.LocalDateTime;
import java.util.List;

public interface TelegramUserService {

    List<TelegramUser> getAllTelegramUsersEnabledIs(boolean enable);

    TelegramUser getById(long id);

    boolean existById(long id);

    void saveTelegramUser(TelegramUser telegramUser);

    void deleteById(long userId);

    void deleteAllByEnabledFalseAndCreatedAtBefore(LocalDateTime currentTime);

}
