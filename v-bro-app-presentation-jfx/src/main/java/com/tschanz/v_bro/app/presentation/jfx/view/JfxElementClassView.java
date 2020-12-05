package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.actions.SelectElementClassAction;
import com.tschanz.v_bro.app.presentation.view.ElementClassView;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementClassItem;
import com.tschanz.v_bro.app.presentation.viewmodel.SelectableItemList;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;


public class JfxElementClassView implements ElementClassView, Initializable {
    @FXML private ComboBox<ElementClassItem> elementClassComboBox;
    private SelectElementClassAction selectElementClassAction;
    private AutoCompletionBinding<ElementClassItem> completionBinding;
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
        if (this.completionBinding != null) {
            this.completionBinding.dispose();
        }
        this.completionBinding = TextFields.bindAutoCompletion(this.elementClassComboBox.getEditor(), this.elementClassComboBox.getItems());
        this.isPopulating = false;
    }


    @FXML
    private void onElementClassSelected(ActionEvent actionEvent) {
        var selectedText = this.elementClassComboBox.getEditor().getText();
        if (!this.isPopulating && selectedText != null) {
            this.elementClassComboBox.getItems()
                .stream()
                .filter(item -> item.getName().equals(selectedText))
                .findFirst()
                .ifPresent(item -> this.selectElementClassAction.next(item.getName()));
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
