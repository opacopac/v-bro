package com.tschanz.v_bro.app.presentation.jfx.presenter;


import com.tschanz.v_bro.app.presentation.presenter.ElementPresenterImpl;
import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;
import com.tschanz.v_bro.app.presenter.element.ElementResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import javafx.application.Platform;


public class JfxElementPresenter extends ElementPresenterImpl {
    public JfxElementPresenter(BehaviorSubject<ElementItem> currentElement) {
        super(currentElement);
    }


    @Override
    public void present(ElementResponse response) {
        Platform.runLater(() -> super.present(response));
    }
}
