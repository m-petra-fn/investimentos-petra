package br.com.petra.service.cache;

import tools.jackson.core.type.TypeReference;

import java.time.Duration;
import java.util.Optional;

public interface CacheStore {

    <T> Optional<T> get(String key, TypeReference<T> typeReference);

    void put(String key, Object value, Duration ttl);

    void evict(String key);

    void evictByPrefix(String keyPrefix);
}


