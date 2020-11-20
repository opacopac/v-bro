package com.tschanz.v_bro.common.reactive;

import lombok.Getter;

import java.util.concurrent.Flow;


public class BehaviorSubject<T> extends Subject<T> {
    @Getter protected T currentValue;


    public BehaviorSubject(T initialValue) {
        this.currentValue = initialValue;
    }


    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("subscriber must not be null");
        }

        super.subscribe(subscriber);
        subscriber.onNext(this.currentValue);
    }


    @Override
    public void next(T newValue) {
        this.currentValue = newValue;
        super.next(newValue);
    }
}
