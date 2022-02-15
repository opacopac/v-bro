package com.tschanz.v_bro.common.cache;


public interface Cache<T> {
    void addItem(String key, T item);

    T getItem(String key);

    void clear();
}
