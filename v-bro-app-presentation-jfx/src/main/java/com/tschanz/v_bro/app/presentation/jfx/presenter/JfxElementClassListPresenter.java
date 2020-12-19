package com.tschanz.v_bro.app.presentation.jfx.presenter;


import com.tschanz.v_bro.app.presentation.presenter.ElementClassListPresenterImpl;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.element_class.ElementClassItem;
import com.tschanz.v_bro.app.presenter.element_class_list.ElementClassListResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;


public class JfxElementClassListPresenter extends ElementClassListPresenterImpl {
    public JfxElementClassListPresenter(BehaviorSubject<SelectableItemList<ElementClassItem>> elementClasses) {
        super(elementClasses);
    }


    @Override
    public void present(ElementClassListResponse response) {
        Platform.runLater(() -> super.present(response));
    }
}
