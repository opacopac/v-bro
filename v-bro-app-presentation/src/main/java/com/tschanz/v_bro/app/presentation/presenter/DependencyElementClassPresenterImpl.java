package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.element_class.ElementClassItem;
import com.tschanz.v_bro.app.presenter.dependency_element_class.DependencyElementClassPresenter;
import com.tschanz.v_bro.app.presenter.element_class.ElementClassResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DependencyElementClassPresenterImpl implements DependencyElementClassPresenter {
    private final BehaviorSubject<SelectableItemList<ElementClassItem>> dependencyElementClasses;


    @Override
    public void present(@NonNull ElementClassResponse response) {
        var elementClasses = ElementClassItem.fromResponse(response);

        this.dependencyElementClasses.next(elementClasses);
    }
}
