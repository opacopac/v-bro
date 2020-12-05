package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.actions.SelectElementClassAction;
import com.tschanz.v_bro.app.presentation.view.ElementClassView;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementClassItem;
import com.tschanz.v_bro.app.presentation.viewmodel.SelectableItemList;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.concurrent.Flow;


public class JfxElementClassView implements ElementClassView {
    @FXML
    private ComboBox<ElementClassItem> elementClassComboBox;
    private SelectElementClassAction selectElementClassAction;
    private boolean isPopulating = false;


    @Override
    public void bindViewModel(
        Flow.Publisher<SelectableItemList<ElementClassItem>> elementClassList,
        SelectElementClassAction selectElementClassAction
    ) {
        this.selectElementClassAction = selectElementClassAction;
        elementClassList.subscribe(new GenericSubscriber<>(this::onElementClassListChanged));
    }


    private void onElementClassListChanged(SelectableItemList<ElementClassItem> elementClassList) {
        this.isPopulating = true;
        this.elementClassComboBox.setItems(FXCollections.observableArrayList(elementClassList.getItems()));
        this.elementClassComboBox.setValue(elementClassList.getSelectedItem());
        this.isPopulating = false;
    }


    @FXML
    private void onElementClassSelected(ActionEvent actionEvent) {
        var selectedItem = this.elementClassComboBox.getValue();
        if (!this.isPopulating && selectedItem != null) {
            this.selectElementClassAction.next(selectedItem.getName());
        }
    }
}
