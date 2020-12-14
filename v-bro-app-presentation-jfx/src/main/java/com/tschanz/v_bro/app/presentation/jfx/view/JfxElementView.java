package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.controller.ElementController;
import com.tschanz.v_bro.app.presentation.view.ElementView;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;
import com.tschanz.v_bro.app.presentation.viewmodel.element.QueryElementItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import lombok.extern.java.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Flow;


@Log
public class JfxElementView implements ElementView, Initializable {
    private static final int DEBOUNCE_TIME_MS = 250;
    @FXML private ComboBox<ElementItem> elementComboBox;
    private ElementController elementController;
    private SuggestionProvider<ElementItem> suggestionProvider;
    private boolean isPopulating = false;
    private String lastQuery;
    private Timer timer;
    private TimerTask timerTask;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.suggestionProvider = SuggestionProvider.create(new ArrayList<>());
        new AutoCompletionTextFieldBinding<>(this.elementComboBox.getEditor(), this.suggestionProvider);
    }


    @Override
    public void bindViewModel(
        Flow.Publisher<SelectableItemList<ElementItem>> elementList,
        ElementController elementController
    ) {
        this.elementController = elementController;
        elementList.subscribe(new GenericSubscriber<>(this::onElementListChanged));
    }


    private void onElementListChanged(SelectableItemList<ElementItem> elementItems) {
        this.isPopulating = true;
        this.elementComboBox.setItems(FXCollections.observableList(elementItems.getItems()));
        this.elementComboBox.setValue(elementItems.getSelectedItem());
        if (this.suggestionProvider != null) {
            this.suggestionProvider.clearSuggestions();
            this.suggestionProvider.addPossibleSuggestions(elementItems.getItems());
            if (this.elementComboBox.getEditor().getOnAction() != null) {
                this.elementComboBox.getEditor().getOnAction().handle(new ActionEvent());
            }
        }
        this.isPopulating = false;
    }


    @FXML private void onElementSelected(ActionEvent actionEvent) {
        var selectedText = this.elementComboBox.getEditor().getText();

        if (!isPopulating && selectedText != null) {
            this.elementComboBox.getItems()
                .stream()
                .filter(item -> item.getName().equals(selectedText))
                .findFirst()
                .ifPresent(selectedItem -> this.elementController.onElementSelected(selectedItem.getId()));

        }
    }


    @FXML public void onKeyReleased(KeyEvent keyEvent) {
        var queryText = this.elementComboBox.getEditor().getText();
        if (queryText.equals(this.lastQuery)) {
            return;
        }
        this.lastQuery = queryText;


        if (this.timer != null) {
            this.timer.cancel();
        }

        if (this.timerTask != null) {
            this.timerTask.cancel();
        }

        this.timerTask = new TimerTask() {
            @Override
            public void run() {
                elementController.onQueryElement(
                    new QueryElementItem(queryText, this.scheduledExecutionTime())
                );
            }
        };

        this.timer = new Timer();
        this.timer.schedule(this.timerTask, DEBOUNCE_TIME_MS);
    }
}
