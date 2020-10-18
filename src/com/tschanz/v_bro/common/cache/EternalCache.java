package com.tschanz.v_bro.common.cache;

import java.util.Dictionary;
import java.util.Hashtable;


public class EternalCache<T> implements Cache<T> {
    private final Dictionary<String, T> cachedItems = new Hashtable<>();


    @Override
    public void addItem(String key, T item) {
        this.cachedItems.put(key, item);
    }


    @Override
    public T getItem(String key) {
        return this.cachedItems.get(key);
    }
}
