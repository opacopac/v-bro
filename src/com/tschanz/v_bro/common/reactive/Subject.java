package com.tschanz.v_bro.common.reactive;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;


public class Subject<T> implements Flow.Publisher<T> {
    protected final List<Flow.Subscriber<? super T>> subscriberList = new ArrayList<>();


    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        this.subscriberList.add(subscriber);
    }


    public void next(T item) {
        for (Flow.Subscriber<? super T> subscriber : this.subscriberList) {
            subscriber.onNext(item);
        }
    }
}
