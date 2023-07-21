package kz.sitehealthtrackerbackend.site_health_tracker_backend.service;


import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.TelegramUser;

import java.util.List;

public interface TelegramUserService {

    TelegramUser findById(long id);

    boolean existById(long id);

    void saveTelegramUser(TelegramUser telegramUser);

    List<TelegramUser> findAllTelegramUsersEnabledIs(boolean enable);

    void deleteById(long userId);
}
