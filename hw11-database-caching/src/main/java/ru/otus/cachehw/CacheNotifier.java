package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;

public class CacheNotifier<K, V> implements HwCacheNotifier<K, V> {
    private final HwCache<K, V> hwCache;
    private List<HwListener<K, V>> listeners = new ArrayList<>();

    CacheNotifier(HwCache<K, V> hwCache) {

        this.hwCache = hwCache;
    }

    @Override
    public void put(K key, V value) {
        hwCache.put(key, value);
        notify(key, null, CacheAction.put);
    }

    @Override
    public void remove(K key) {
        hwCache.remove(key);
        notify(key, null, CacheAction.remove);
    }

    @Override
    public V get(K key) {
        return hwCache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    };

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    };

    private void notify(K key, V value, CacheAction action) {
        listeners.forEach(listener -> listener.notify(key, value, action.name()));
    }
}
