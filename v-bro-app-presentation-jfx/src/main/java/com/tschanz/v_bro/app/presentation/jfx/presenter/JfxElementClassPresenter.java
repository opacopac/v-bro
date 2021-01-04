package com.tschanz.v_bro.app.presentation.jfx.presenter;


import com.tschanz.v_bro.app.presentation.presenter.ElementClassPresenterImpl;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.element_class.ElementClassItem;
import com.tschanz.v_bro.app.presenter.element_class.ElementClassResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;


public class JfxElementClassPresenter extends ElementClassPresenterImpl {
    public JfxElementClassPresenter(BehaviorSubject<SelectableItemList<ElementClassItem>> elementClasses) {
        super(elementClasses);
    }


    @Override
    public void present(ElementClassResponse response) {
        Platform.runLater(() -> super.present(response));
    }
}
