package ru.otus.cachehw;


public class NonExpiringCache<K, V> extends BaseCache<K, V> {
    @Override
    protected boolean isValid(K key) {
        return true;
    };
}
