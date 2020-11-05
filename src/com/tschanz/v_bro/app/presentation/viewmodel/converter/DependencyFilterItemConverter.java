package com.tschanz.v_bro.app.presentation.viewmodel.converter;

import com.tschanz.v_bro.app.presentation.viewmodel.DependencyFilterItem;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.requestmodel.DependencyFilterRequest;


public class DependencyFilterItemConverter {
    public static DependencyFilterRequest toRequest(DependencyFilterItem dependencyFilter) {
        return new DependencyFilterRequest(dependencyFilter.isFwd);
    }
}
