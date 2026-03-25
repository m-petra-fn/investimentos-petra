package br.com.petra.config.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableConfigurationProperties(CacheRedisProperties.class)
public class CacheConfig {

    @Bean
    @ConditionalOnProperty(prefix = "app.cache.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
    CacheManager redisCacheManager(RedisConnectionFactory connectionFactory, CacheRedisProperties properties) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(properties.getTtl())
                .computePrefixWith(cacheName -> properties.getKeyPrefix() + ":" + cacheName + ":")
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.cache.redis", name = "enabled", havingValue = "false")
    CacheManager noOpCacheManager() {
        return new NoOpCacheManager();
    }
}


