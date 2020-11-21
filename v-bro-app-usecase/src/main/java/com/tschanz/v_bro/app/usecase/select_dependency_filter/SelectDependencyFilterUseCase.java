package com.tschanz.v_bro.app.usecase.select_dependency_filter;


import com.tschanz.v_bro.app.usecase.select_dependency_filter.requestmodel.SelectDependencyFilterRequest;

public interface SelectDependencyFilterUseCase {
    void execute(SelectDependencyFilterRequest request);
}
