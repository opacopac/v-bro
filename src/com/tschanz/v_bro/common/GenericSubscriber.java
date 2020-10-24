package com.tschanz.v_bro.common;

import java.util.concurrent.Flow;
import java.util.function.Consumer;


public class GenericSubscriber<T> implements Flow.Subscriber<T> {
    protected final Consumer<T> consumerFunction;


    public GenericSubscriber(Consumer<T> consumerFunction) {
        this.consumerFunction = consumerFunction;
    }


    @Override
    public void onSubscribe(Flow.Subscription subscription) {
    }


    @Override
    public void onNext(T item) {
        this.consumerFunction.accept(item);
    }


    @Override
    public void onError(Throwable throwable) {
    }


    @Override
    public void onComplete() {
    }
}
