package com.tschanz.v_bro.common;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class Triple<T, K, R> {
    public final T first;
    public final K second;
    public final R third;
}
