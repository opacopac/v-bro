package com.tschanz.v_bro.common.reactive;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.Flow;
import java.util.function.Consumer;


@RequiredArgsConstructor
public class GenericSubscriber<T> implements Flow.Subscriber<T> {
    protected final Consumer<T> consumerFunction;


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
