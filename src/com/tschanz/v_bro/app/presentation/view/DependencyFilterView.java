package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.DependencyFilterItem;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;

import java.util.concurrent.Flow;


public interface DependencyFilterView {
    void bindInitialDependencyFilter(Flow.Publisher<DependencyFilterItem> dependencyFilter);

    void bindSelectDependencyFilterAction(BehaviorSubject<DependencyFilterItem> selectDependencyFilterAction);
}
