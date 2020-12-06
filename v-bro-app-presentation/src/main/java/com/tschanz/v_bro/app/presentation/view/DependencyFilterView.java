package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.viewmodel.DependencyFilterItem;

import java.util.concurrent.Flow;


public interface DependencyFilterView {
    void bindViewModel(
        Flow.Publisher<DependencyFilterItem> dependencyFilter,
        ViewAction<DependencyFilterItem> selectDependencyFilterAction
    );
}
