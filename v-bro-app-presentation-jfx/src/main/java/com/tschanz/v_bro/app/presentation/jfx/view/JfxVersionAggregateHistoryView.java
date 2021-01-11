package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.controller.VersionAggregateHistoryController;
import com.tschanz.v_bro.app.presentation.view.VersionAggregateHistoryView;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;
import com.tschanz.v_bro.app.presentation.viewmodel.element_class.ElementClassItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version_aggregate_history.VersionAggregateHistoryItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.concurrent.Flow;


public class JfxVersionAggregateHistoryView implements VersionAggregateHistoryView {
    private static final String NOT_SELECTED_VALUE = "-";
    @FXML private Button backButton;
    @FXML private Button fwdButton;
    @FXML private Label elementClassLabel;
    @FXML private Label elementIdLabel;
    @FXML private Label versionIdLabel;
    private VersionAggregateHistoryController versionAggregateHistoryController;


    @Override
    public void bindViewModel(
        Flow.Publisher<SelectableItemList<ElementClassItem>> elementClasses,
        Flow.Publisher<ElementItem> currentElement,
        Flow.Publisher<SelectableItemList<VersionItem>> versions,
        Flow.Publisher<VersionAggregateHistoryItem> versionAggregateHistory,
        VersionAggregateHistoryController versionAggregateHistoryController
    ) {
        this.versionAggregateHistoryController = versionAggregateHistoryController;
        elementClasses.subscribe(new GenericSubscriber<>(this::onElementClassesChanged));
        currentElement.subscribe(new GenericSubscriber<>(this::onCurrentElementChanged));
        versions.subscribe(new GenericSubscriber<>(this::onVersionsChanged));
        versionAggregateHistory.subscribe(new GenericSubscriber<>(this::onVersionAggregateHistoryChanged));
    }


    private void onElementClassesChanged(SelectableItemList<ElementClassItem> elementClasses) {
        var currentElementClass = elementClasses.getSelectedItem();
        if (currentElementClass != null) {
            this.elementClassLabel.setText(currentElementClass.getName());
        } else {
            this.elementClassLabel.setText(NOT_SELECTED_VALUE);
        }
    }


    private void onCurrentElementChanged(ElementItem element) {
        if (element != null) {
            this.elementIdLabel.setText(element.getId());
        } else {
            this.elementClassLabel.setText(NOT_SELECTED_VALUE);
        }
    }


    private void onVersionsChanged(SelectableItemList<VersionItem> versions) {
        var currentVersion = versions.getSelectedItem();
        if (currentVersion != null) {
            this.versionIdLabel.setText(currentVersion.getId());
        } else {
            this.versionIdLabel.setText(NOT_SELECTED_VALUE);
        }
    }


    private void onVersionAggregateHistoryChanged(VersionAggregateHistoryItem versionAggregateHistory) {
        this.backButton.setDisable(!versionAggregateHistory.hasPrevious());
        this.fwdButton.setDisable(!versionAggregateHistory.hasNext());
    }


    @FXML private void onBackClicked(ActionEvent actionEvent) {
        new Thread(() -> {
            this.versionAggregateHistoryController.goBack();
        }).start();
    }


    @FXML private void onForwardClicked(ActionEvent actionEvent) {
        new Thread(() -> {
            this.versionAggregateHistoryController.goForward();
        }).start();
    }
}
