package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.actions.SelectElementAction;
import com.tschanz.v_bro.app.presentation.view.ElementView;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementItem;
import com.tschanz.v_bro.app.presentation.viewmodel.SelectableItemList;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.concurrent.Flow;


public class JfxElementView implements ElementView {
    @FXML private ComboBox<ElementItem> elementComboBox;
    private SelectElementAction selectElementAction;
    //private AutoCompletionBinding<String> autoCompletionBinding;
    private boolean isPopulating = false;


    /*public void initialize(URL url, ResourceBundle resourceBundle) {
        if (this.autoCompletionBinding != null) {
            this.autoCompletionBinding.dispose();
        }

        this.autoCompletionBinding = TextFields.bindAutoCompletion(this.elementTextField, "Hey", "Hello", "Hello World", "Apple", "Cool", "Costa", "Cola", "Coca Cola");
    }*/


    @Override
    public void bindViewModel(
        Flow.Publisher<SelectableItemList<ElementItem>> elementList,
        SelectElementAction selectElementAction
    ) {
        this.selectElementAction = selectElementAction;
        elementList.subscribe(new GenericSubscriber<>(this::onElementListChanged));
    }


    private void onElementListChanged(SelectableItemList<ElementItem> elementItems) {
        this.isPopulating = true;
        this.elementComboBox.setItems(FXCollections.observableList(elementItems.getItems()));
        this.elementComboBox.setValue(elementItems.getSelectedItem());
        this.isPopulating = false;
    }


    @FXML private void onElementSelected(ActionEvent actionEvent) {
        var selectedElement = this.elementComboBox.getValue();

        if (!isPopulating && selectedElement != null) {
            this.selectElementAction.next(selectedElement.getId());
        }
    }
}
