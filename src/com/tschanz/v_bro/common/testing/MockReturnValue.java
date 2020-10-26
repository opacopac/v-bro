package com.tschanz.v_bro.common.testing;

import java.util.ArrayList;


public class MockReturnValue<T> {
    private final String argumentName;
    private final ArrayList<T> returnValueList = new ArrayList<>();


    public MockReturnValue(String argumentName) {
        this.argumentName = argumentName;
    }


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
