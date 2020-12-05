package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.actions.SelectDenominationsAction;
import com.tschanz.v_bro.app.presentation.view.ElementDenominationView;
import com.tschanz.v_bro.app.presentation.viewmodel.DenominationItem;
import com.tschanz.v_bro.app.presentation.viewmodel.MultiSelectableItemList;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class JfxElementDenominationView implements Initializable, ElementDenominationView {
    @FXML public CheckComboBox<DenominationItem> denominationCheckComboBox;
    private SelectDenominationsAction selectDenominationsAction;
    private boolean isPopulating = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.denominationCheckComboBox.addEventHandler(ComboBox.ON_HIDDEN, this::onDenominationSelected);
    }


    @Override
    public void bindViewModel(
        Flow.Publisher<MultiSelectableItemList<DenominationItem>> denominationsList,
        SelectDenominationsAction selectDenominationsAction
    ) {
        this.selectDenominationsAction = selectDenominationsAction;
        denominationsList.subscribe(new GenericSubscriber<>(this::onDenominationsListChanged));
    }


    private void onDenominationsListChanged(MultiSelectableItemList<DenominationItem> denominationItems) {
        this.isPopulating = true;
        this.denominationCheckComboBox.getItems().setAll(FXCollections.observableArrayList(denominationItems.getItems()));
        var checkModel = this.denominationCheckComboBox.getCheckModel();
        for (int i = 0; i < denominationItems.getItems().size(); i++) {
            var item = denominationItems.getItems().get(i);
            if (denominationItems.getSelectedItems().contains(item)) {
                checkModel.check(i);
            }
        }
        this.isPopulating = false;
    }


    private void onDenominationSelected(Event e) {
        if (!this.isPopulating && this.selectDenominationsAction != null) {
            var selectedItems = IntStream.range(0, this.denominationCheckComboBox.getItems().size())
                .filter(i -> this.denominationCheckComboBox.getCheckModel().isChecked(i))
                .mapToObj(i -> this.denominationCheckComboBox.getCheckModel().getItem(i))
                .collect(Collectors.toList());

            this.selectDenominationsAction.next(selectedItems);
        }
    }
}
