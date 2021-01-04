package com.tschanz.v_bro.common.types;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class Pair<T, K> {
    public final T first;
    public final K second;
}
