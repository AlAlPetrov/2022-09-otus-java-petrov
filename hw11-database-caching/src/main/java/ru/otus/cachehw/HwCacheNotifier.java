package ru.otus.cachehw;


public interface HwCacheNotifier<K, V> extends HwCache<K, V> {
    void addListener(HwListener<K, V> listener);

    void removeListener(HwListener<K, V> listener);
}
