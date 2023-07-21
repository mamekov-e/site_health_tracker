package kz.sitehealthtracker.site_health_tracker.service.impls;

import kz.sitehealthtracker.site_health_tracker.model.TelegramUser;
import kz.sitehealthtracker.site_health_tracker.repository.TelegramUserRepository;
import kz.sitehealthtracker.site_health_tracker.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

import static kz.sitehealthtracker.site_health_tracker.constants.CacheConstants.ENABLED_TELEGRAM_USERS_CACHE_NAME;
import static kz.sitehealthtracker.site_health_tracker.constants.CacheConstants.TELEGRAM_USER_CACHE_NAME;

@Service
public class TelegramUserServiceImpl implements TelegramUserService {

    @Autowired
    private TelegramUserRepository telegramUserRepository;

    @Cacheable(cacheNames = ENABLED_TELEGRAM_USERS_CACHE_NAME)
    @Override
    public List<TelegramUser> findAllTelegramUsersEnabledIs(boolean enable) {
        return telegramUserRepository.findAllByEnabledIs(enable);
    }

    @Cacheable(cacheNames = TELEGRAM_USER_CACHE_NAME, key = "#id", unless = "#result == null")
    @Override
    public TelegramUser findById(long id) {
        return telegramUserRepository.findById(id)
                .orElse(null);
    }

    @Override
    public boolean existById(long id) {
        return findById(id) != null;
    }


    @Caching(evict = {
            @CacheEvict(cacheNames = TELEGRAM_USER_CACHE_NAME, key = "#telegramUser.id"),
            @CacheEvict(cacheNames = ENABLED_TELEGRAM_USERS_CACHE_NAME, allEntries = true)
    })
    @Override
    public void saveTelegramUser(TelegramUser telegramUser) {
        telegramUserRepository.save(telegramUser);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = TELEGRAM_USER_CACHE_NAME, key = "#id"),
            @CacheEvict(cacheNames = ENABLED_TELEGRAM_USERS_CACHE_NAME, allEntries = true)
    })
    @Override
    public void deleteById(long id) {
        telegramUserRepository.deleteById(id);
    }

}
