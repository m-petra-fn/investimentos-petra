package br.com.petra.config.cache;

import br.com.petra.service.cache.CacheStore;
import br.com.petra.service.cache.NoOpCacheStore;
import br.com.petra.service.cache.RedisCacheStore;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@EnableConfigurationProperties(CacheRedisProperties.class)
public class CacheConfig {

    @Bean
    @ConditionalOnProperty(prefix = "app.cache.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
    CacheStore redisCacheStore(StringRedisTemplate redisTemplate, JsonMapper objectMapper) {
        return new RedisCacheStore(redisTemplate, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(CacheStore.class)
    CacheStore noOpCacheStore() {
        return new NoOpCacheStore();
    }
}


