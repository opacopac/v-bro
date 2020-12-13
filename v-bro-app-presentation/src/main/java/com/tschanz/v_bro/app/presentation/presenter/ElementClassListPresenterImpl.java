package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.element_class.ElementClassItem;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presenter.element_class_list.ElementClassListPresenter;
import com.tschanz.v_bro.app.presenter.element_class_list.ElementClassListResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ElementClassListPresenterImpl implements ElementClassListPresenter {
    private final BehaviorSubject<SelectableItemList<ElementClassItem>> elementClasses;


    @Override
    public void present(@NonNull ElementClassListResponse response) {
        var elements = ElementClassItem.fromResponse(response);

        this.elementClasses.next(elements);
    }
}
