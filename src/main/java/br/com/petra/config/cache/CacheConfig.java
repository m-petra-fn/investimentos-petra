package br.com.petra.config.cache;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.util.Set;

@Configuration
@EnableConfigurationProperties(CacheRedisProperties.class)
public class CacheConfig {

    @Bean
    @ConditionalOnProperty(prefix = "app.cache.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
    CacheManager redisCacheManager(RedisConnectionFactory connectionFactory, CacheRedisProperties properties, MeterRegistry meterRegistry) {
        GenericJacksonJsonRedisSerializer serializer = GenericJacksonJsonRedisSerializer.builder()
                .typePropertyName("@class")
                .enableUnsafeDefaultTyping()
                .build();

        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(properties.getTtl())
                .computePrefixWith(cacheName -> properties.getKeyPrefix() + ":" + cacheName + ":")
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));

        Set<String> cacheNames = Set.of(
                "investimento-by-id",
                "investimento-all",
                "rendimento-by-id",
                "rendimento-all",
                "rendimento-by-investimento"
        );

        CacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .initialCacheNames(cacheNames)
                .enableStatistics()
                .build();

        return new MeteredCacheManager(redisCacheManager, meterRegistry);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.cache.redis", name = "enabled", havingValue = "false")
    CacheManager noOpCacheManager(MeterRegistry meterRegistry) {
        return new MeteredCacheManager(new NoOpCacheManager(), meterRegistry);
    }
}


