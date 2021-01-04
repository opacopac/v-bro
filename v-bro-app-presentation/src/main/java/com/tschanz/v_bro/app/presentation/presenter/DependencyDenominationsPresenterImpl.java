package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.common.MultiSelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.denominations.DenominationItem;
import com.tschanz.v_bro.app.presenter.denominations.DenominationListResponse;
import com.tschanz.v_bro.app.presenter.dependency_denominations.DependencyDenominationsPresenter;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DependencyDenominationsPresenterImpl implements DependencyDenominationsPresenter {
    public final BehaviorSubject<MultiSelectableItemList<DenominationItem>> dependencyDenominations;


    @Override
    public void present(@NonNull DenominationListResponse response) {
        var denominations = DenominationItem.fromResponse(response);

        this.dependencyDenominations.next(denominations);
    }
}
