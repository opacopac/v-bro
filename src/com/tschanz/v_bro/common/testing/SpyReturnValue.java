package com.tschanz.v_bro.common.testing;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;


@RequiredArgsConstructor
public class SpyReturnValue<T> {
    private final String argumentName;
    private final ArrayList<T> returnValueList = new ArrayList<>();


    public void add(T returnValue) {
        this.returnValueList.add(returnValue);
    }


    public T next() {
        if (this.returnValueList.size() == 0) {
            throw new IllegalStateException("error: not enough mock return values for argument " + this.argumentName);
        }

        return this.returnValueList.remove(0);
    }
}
