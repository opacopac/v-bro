package com.tschanz.v_bro.app.presentation.jfx.presenter;

import com.tschanz.v_bro.app.presentation.presenter.ElementListPresenterImpl;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;
import com.tschanz.v_bro.app.presenter.element_list.ElementListResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;
import lombok.NonNull;


public class JfxElementListPresenter extends ElementListPresenterImpl {
    public JfxElementListPresenter(BehaviorSubject<SelectableItemList<ElementItem>> elements) {
        super(elements);
    }


    @Override
    public void present(@NonNull ElementListResponse response) {
        Platform.runLater(() -> super.present(response));
    }
}
