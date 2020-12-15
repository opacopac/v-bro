package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;
import com.tschanz.v_bro.app.presenter.element.ElementPresenter;
import com.tschanz.v_bro.app.presenter.element.ElementResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ElementPresenterImpl implements ElementPresenter {
    private final BehaviorSubject<ElementItem> currentElement;


    @Override
    public void present(@NonNull ElementResponse response) {
        var element = ElementItem.fromResponse(response);

        this.currentElement.next(element);
    }
}
