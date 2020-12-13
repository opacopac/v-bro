package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.dependency.FwdDependencyItem;
import com.tschanz.v_bro.app.presenter.dependencies.DependencyListResponse;
import com.tschanz.v_bro.app.presenter.dependencies.DependencyPresenter;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class DependenciesPresenterImpl implements DependencyPresenter {
    public final BehaviorSubject<List<FwdDependencyItem>> fwdDependencies;


    @Override
    public void present(@NonNull DependencyListResponse response) {
        var elements = FwdDependencyItem.fromResponse(response);

        this.fwdDependencies.next(elements);
    }
}
