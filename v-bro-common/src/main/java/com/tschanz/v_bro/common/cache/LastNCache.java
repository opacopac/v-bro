package com.tschanz.v_bro.common.cache;

import com.tschanz.v_bro.common.types.Pair;

import java.util.ArrayList;
import java.util.List;


public class LastNCache<T> implements Cache<T> {
    private final List<Pair<String, T>> cachedItems = new ArrayList<>();
    private final int maxCacheSize;


    public LastNCache(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }


    @Override
    public void addItem(String key, T item) {
        if (this.getItem(key) == null) {
            this.cachedItems.add(new Pair<>(key, item));

            while (this.cachedItems.size() > this.maxCacheSize) {
                this.cachedItems.remove(0);
            }
        }
    }


    @Override
    public T getItem(String key) {
        return this.cachedItems
            .stream()
            .filter(item -> item.first.equals(key))
            .map(item -> item.second)
            .findFirst()
            .orElse(null);
    }


    @Override
    public void clear() {
        this.cachedItems.clear();
    }
}
