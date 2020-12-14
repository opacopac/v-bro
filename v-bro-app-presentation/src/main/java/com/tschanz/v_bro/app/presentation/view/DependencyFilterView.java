package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.controller.DependencyFilterController;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.DependencyFilterItem;

import java.util.concurrent.Flow;


public interface DependencyFilterView {
    void bindViewModel(
        Flow.Publisher<DependencyFilterItem> dependencyFilter,
        DependencyFilterController dependencyFilterController
    );
}
