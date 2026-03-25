package br.com.petra.service.cache;

import tools.jackson.core.type.TypeReference;

import java.time.Duration;
import java.util.Optional;

public class NoOpCacheStore implements CacheStore {

    @Override
    public <T> Optional<T> get(String key, TypeReference<T> typeReference) {
        return Optional.empty();
    }

    @Override
    public void put(String key, Object value, Duration ttl) {
        // Intencionalmente vazio para desabilitar cache sem alterar servicos.
    }

    @Override
    public void evict(String key) {
        // Intencionalmente vazio para desabilitar cache sem alterar servicos.
    }

    @Override
    public void evictByPrefix(String keyPrefix) {
        // Intencionalmente vazio para desabilitar cache sem alterar servicos.
    }
}


