package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.DependencyFilterItem;
import com.tschanz.v_bro.app.presentation.actions.SelectDependencyFilterAction;

import java.util.concurrent.Flow;


public interface DependencyFilterView {
    void bindViewModel(
        Flow.Publisher<DependencyFilterItem> dependencyFilter,
        SelectDependencyFilterAction selectDependencyFilterAction
    );
}
