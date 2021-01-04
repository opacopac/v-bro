package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.controller.DependencyElementClassController;
import com.tschanz.v_bro.app.presentation.view.DependencyElementClassView;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.element_class.ElementClassItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import lombok.extern.java.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;


@Log
public class JfxDependencyElementClassView implements DependencyElementClassView, Initializable {
    @FXML private ComboBox<ElementClassItem> elementClassComboBox;
    private DependencyElementClassController dependencyElementClassController;
    private SuggestionProvider<ElementClassItem> suggestionProvider;
    private String lastQuery;
    private boolean isPopulating = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.suggestionProvider = SuggestionProvider.create(new ArrayList<>());
        this.suggestionProvider.setShowAllIfEmpty(true);
        new AutoCompletionTextFieldBinding<>(this.elementClassComboBox.getEditor(), this.suggestionProvider);
    }


    @Override
    public void bindViewModel(
        Flow.Publisher<SelectableItemList<ElementClassItem>> elementClassList,
        DependencyElementClassController dependencyElementClassController
    ) {
        this.dependencyElementClassController = dependencyElementClassController;
        elementClassList.subscribe(new GenericSubscriber<>(this::onElementClassListChanged));
    }


    private void onElementClassListChanged(SelectableItemList<ElementClassItem> elementClassList) {
        this.isPopulating = true;
        this.elementClassComboBox.setItems(FXCollections.observableArrayList(elementClassList.getItems()));
        this.elementClassComboBox.setValue(elementClassList.getSelectedItem());
        if (this.suggestionProvider != null) {
            this.suggestionProvider.clearSuggestions();
            this.suggestionProvider.addPossibleSuggestions(elementClassList.getItems());
        }
        this.isPopulating = false;
    }


    @FXML
    private void onElementClassSelected(ActionEvent actionEvent) {
        var queryText = this.elementClassComboBox.getEditor().getText();
        if (queryText.equals(this.lastQuery)) {
            return;
        }
        this.lastQuery = queryText;

        var selectedText = this.elementClassComboBox.getEditor().getText();
        if (!this.isPopulating && selectedText != null) {
            new Thread(() -> {
                this.elementClassComboBox.getItems()
                    .stream()
                    .filter(item -> item.getName().equals(selectedText))
                    .findFirst()
                    .ifPresent(item -> this.dependencyElementClassController.onElementClassSelected(item.getName()));
            }).start();
        }
    }
}
