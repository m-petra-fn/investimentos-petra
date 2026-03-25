package br.com.petra.service.cache;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
public class RedisCacheStore implements CacheStore {

    private final StringRedisTemplate redisTemplate;
    private final JsonMapper objectMapper;

    @Override
    public <T> Optional<T> get(String key, TypeReference<T> typeReference) {
        try {
            String raw = redisTemplate.opsForValue().get(key);
            if (raw == null) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(raw, typeReference));
        } catch (Exception exception) {
            log.warn("Falha ao ler chave de cache {}. Seguindo sem cache.", key, exception);
            return Optional.empty();
        }
    }

    @Override
    public void put(String key, Object value, Duration ttl) {
        try {
            String raw = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, raw, ttl);
        } catch (Exception exception) {
            log.warn("Falha ao gravar chave de cache {}. Seguindo sem cache.", key, exception);
        }
    }

    @Override
    public void evict(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception exception) {
            log.warn("Falha ao remover chave de cache {}. Seguindo sem cache.", key, exception);
        }
    }

    @Override
    public void evictByPrefix(String keyPrefix) {
        try {
            Set<String> keys = redisTemplate.keys(keyPrefix + "*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } catch (Exception exception) {
            log.warn("Falha ao invalidar cache por prefixo {}. Seguindo sem cache.", keyPrefix, exception);
        }
    }
}


