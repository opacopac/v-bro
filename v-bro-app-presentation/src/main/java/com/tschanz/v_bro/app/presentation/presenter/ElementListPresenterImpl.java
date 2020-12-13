package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presenter.element_list.ElementListPresenter;
import com.tschanz.v_bro.app.presenter.element_list.ElementListResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ElementListPresenterImpl implements ElementListPresenter {
    private final BehaviorSubject<SelectableItemList<ElementItem>> elements;


    @Override
    public void present(@NonNull ElementListResponse response) {
        var elements = ElementItem.fromResponse(response);

        this.elements.next(elements);
    }
}
