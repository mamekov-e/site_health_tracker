package kz.sitehealthtrackerbackend.site_health_tracker_backend.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.CacheConstants.*;

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
    public RedisCacheManager redisCacheManager(ObjectMapper objectMapper) {
        RedisCacheConfiguration redisCacheConfiguration = cacheConfiguration(objectMapper, REDIS_CACHE_DURATION);

        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(redisCacheConfiguration)
                .withCacheConfiguration(SITE_CACHE_NAME,
                        cacheConfiguration(objectMapper, SITE_CACHE_DURATION))
                .withCacheConfiguration(SITE_GROUP_CACHE_NAME,
                        cacheConfiguration(objectMapper, SITE_GROUP_CACHE_DURATION))
                .withCacheConfiguration(SITES_OF_GROUP_CACHE_NAME,
                        cacheConfiguration(objectMapper, SITES_OF_GROUP_CACHE_DURATION))
                .withCacheConfiguration(GROUPS_OF_SITE_CACHE_NAME,
                        cacheConfiguration(objectMapper, GROUPS_OF_SITE_CACHE_DURATION))
                .withCacheConfiguration(ENABLED_EMAILS_CACHE_NAME,
                        cacheConfiguration(objectMapper, ENABLED_EMAILS_CACHE_DURATION))
                .withCacheConfiguration(TELEGRAM_USER_CACHE_NAME,
                        cacheConfiguration(objectMapper, TELEGRAM_USER_CACHE_DURATION))
                .withCacheConfiguration(ENABLED_TELEGRAM_USERS_CACHE_NAME,
                        cacheConfiguration(objectMapper, ENABLED_TELEGRAM_USERS_CACHE_DURATION))
                .build();
    }

    private RedisCacheConfiguration cacheConfiguration(ObjectMapper mapper, Duration ttl) {
        ObjectMapper myMapper = mapper.copy()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .activateDefaultTyping(new ObjectMapper().getPolymorphicTypeValidator(),
                        ObjectMapper.DefaultTyping.EVERYTHING,
                        JsonTypeInfo.As.PROPERTY);
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(myMapper)))
                .disableCachingNullValues();
    }
}
