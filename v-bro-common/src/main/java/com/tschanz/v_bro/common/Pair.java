package com.tschanz.v_bro.common;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class Pair<T, K> {
    public final T first;
    public final K second;
}
