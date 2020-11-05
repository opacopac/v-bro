package com.tschanz.v_bro.app.presentation.viewmodel.actions;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;


public abstract class ViewAction<T> extends BehaviorSubject<T> {
    public ViewAction(T initialElementId) {
        super(initialElementId);
    }
}
