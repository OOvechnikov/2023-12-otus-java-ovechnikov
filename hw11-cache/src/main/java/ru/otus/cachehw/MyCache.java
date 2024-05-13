package ru.otus.cachehw;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

@Slf4j
public class MyCache<K, V> implements HwCache<K, V> {

    private final WeakHashMap<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V value = cache.get(key);
        cache.remove(key);
        notifyListeners(key, value, "remove");
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        notifyListeners(key, value, "get");
        return value;
    }

    private void notifyListeners(K key, V value, String action) {
        listeners.forEach(l -> {
            try {
                l.notify(key, value, action);
            } catch (Exception e) {
                log.warn("Unable to notify listener: {} for action: {} with key: {} and value: {}", l, action, key, value);
            }
        });
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

}
