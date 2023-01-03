package ru.otus.cachehw;


import java.lang.ref.WeakReference;
import java.util.HashMap;

abstract class BaseCache<K, V> implements HwCache<K, V> {
    private HashMap<K, WeakReference<V>> cachedData = new HashMap<>();

    @Override
    public void put(K key, V value) {
        cachedData.put(key, new WeakReference(value));
    }

    @Override
    public void remove(K key) {
        cachedData.remove(key);
    }

    @Override
    public V get(K key) {
        if (isValid(key)) {
            return cachedData.get(key).get();
        }
        remove(key);
        return null;
    }

    abstract boolean isValid(K key);
}
