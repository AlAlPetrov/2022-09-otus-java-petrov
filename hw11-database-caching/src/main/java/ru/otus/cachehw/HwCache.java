package ru.otus.cachehw;


public interface HwCache<K, V> {

    void put(K key, V value);

    void remove(K key);

    V get(K key);
}
