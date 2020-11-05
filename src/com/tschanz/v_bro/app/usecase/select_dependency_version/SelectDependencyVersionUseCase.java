package com.tschanz.v_bro.app.usecase.select_dependency_version;


import com.tschanz.v_bro.app.usecase.select_dependency_version.requestmodel.SelectDependencyVersionRequest;

public interface SelectDependencyVersionUseCase {
    void execute(SelectDependencyVersionRequest request);
}
