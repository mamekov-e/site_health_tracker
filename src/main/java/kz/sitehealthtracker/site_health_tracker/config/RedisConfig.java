package kz.sitehealthtracker.site_health_tracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

import static kz.sitehealthtracker.site_health_tracker.constants.CacheConstants.*;

@Configuration
@AutoConfigureAfter({RedisAutoConfiguration.class})
@EnableCaching
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheConfiguration redisCacheConfiguration = configuredRedisCache(REDIS_CACHE_DURATION);

        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(redisCacheConfiguration)
                .withCacheConfiguration(SITES_CACHE_NAME,
                        configuredRedisCache(SITES_CACHE_DURATION))
                .withCacheConfiguration(SITE_CACHE_NAME,
                        configuredRedisCache(SITE_CACHE_DURATION))
                .withCacheConfiguration(SITE_GROUPS_CACHE_NAME,
                        configuredRedisCache(SITE_GROUPS_CACHE_DURATION))
                .withCacheConfiguration(SITE_GROUP_CACHE_NAME,
                        configuredRedisCache(SITE_GROUP_CACHE_DURATION))
                .withCacheConfiguration(SITES_OF_GROUP_CACHE_NAME,
                        configuredRedisCache(SITES_OF_GROUP_CACHE_DURATION))
                .withCacheConfiguration(GROUPS_OF_SITE_CACHE_NAME,
                        configuredRedisCache(GROUPS_OF_SITE_CACHE_DURATION))
                .withCacheConfiguration(ENABLED_EMAILS_CACHE_NAME,
                        configuredRedisCache(ENABLED_EMAILS_CACHE_DURATION))
                .withCacheConfiguration(TELEGRAM_USER_CACHE_NAME,
                        configuredRedisCache(TELEGRAM_USER_CACHE_DURATION))
                .withCacheConfiguration(ENABLED_TELEGRAM_USERS_CACHE_NAME,
                        configuredRedisCache(ENABLED_TELEGRAM_USERS_CACHE_DURATION))
                .build();
    }

    private RedisCacheConfiguration configuredRedisCache(Duration duration) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(duration)
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();
    }
}
