package kz.sitehealthtracker.site_health_tracker.service;


import kz.sitehealthtracker.site_health_tracker.model.TelegramUser;

import java.util.List;

public interface TelegramUserService {

    TelegramUser findById(long id);

    boolean existById(long id);

    void saveTelegramUser(TelegramUser telegramUser);

    List<TelegramUser> findAllTelegramUsersEnabledIs(boolean enable);

    void deleteById(long userId);
}
