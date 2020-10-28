package com.tschanz.v_bro.dependencies.presentation.view;

import com.tschanz.v_bro.dependencies.presentation.viewmodel.FwdDependencyItem;

import java.util.List;
import java.util.concurrent.Flow;


public interface DependenciesView {
    void bindFwdDependencyList(Flow.Publisher<List<FwdDependencyItem>> fwdDependencyList);
}
