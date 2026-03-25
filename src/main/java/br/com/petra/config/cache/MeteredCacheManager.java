package br.com.petra.config.cache;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MeteredCacheManager implements CacheManager {

    private final CacheManager delegate;
    private final MeterRegistry meterRegistry;
    private final Map<String, Cache> wrappedCaches = new ConcurrentHashMap<>();

    public MeteredCacheManager(CacheManager delegate, MeterRegistry meterRegistry) {
        this.delegate = delegate;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = delegate.getCache(name);
        if (cache == null) {
            return null;
        }
        return wrappedCaches.computeIfAbsent(name, key -> new MeteredCache(cache, meterRegistry));
    }

    @Override
    public Collection<String> getCacheNames() {
        return delegate.getCacheNames();
    }
}

