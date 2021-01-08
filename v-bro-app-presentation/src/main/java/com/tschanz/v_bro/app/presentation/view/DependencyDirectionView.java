package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.controller.DependencyDirectionController;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.DependencyDirectionItem;

import java.util.concurrent.Flow;


public interface DependencyDirectionView {
    void bindViewModel(
        Flow.Publisher<DependencyDirectionItem> dependencyDirection,
        DependencyDirectionController dependencyDirectionController
    );
}
