package br.com.petra.config.cache;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

public class MeteredCache implements Cache {

    private final Cache delegate;
    private final Counter hitCounter;
    private final Counter missCounter;

    public MeteredCache(Cache delegate, MeterRegistry meterRegistry) {
        this.delegate = delegate;
        this.hitCounter = Counter.builder("cache.gets")
                .tag("cache", delegate.getName())
                .tag("result", "hit")
                .register(meterRegistry);
        this.missCounter = Counter.builder("cache.gets")
                .tag("cache", delegate.getName())
                .tag("result", "miss")
                .register(meterRegistry);
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public Object getNativeCache() {
        return delegate.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper value = delegate.get(key);
        if (value == null) {
            missCounter.increment();
        } else {
            hitCounter.increment();
        }
        return value;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        T value = delegate.get(key, type);
        if (value == null) {
            missCounter.increment();
        } else {
            hitCounter.increment();
        }
        return value;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return delegate.get(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        delegate.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return delegate.putIfAbsent(key, value);
    }

    @Override
    public void evict(Object key) {
        delegate.evict(key);
    }

    @Override
    public boolean evictIfPresent(Object key) {
        return delegate.evictIfPresent(key);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean invalidate() {
        return delegate.invalidate();
    }
}

