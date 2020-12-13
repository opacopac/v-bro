package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.view.ElementDenominationView;
import com.tschanz.v_bro.app.presentation.viewmodel.denominations.DenominationItem;
import com.tschanz.v_bro.app.presentation.viewmodel.common.MultiSelectableItemList;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class JfxElementDenominationView implements Initializable, ElementDenominationView {
    @FXML public CheckComboBox<DenominationItem> denominationCheckComboBox;
    private ViewAction<List<DenominationItem>> selectDenominationsAction;
    private boolean isPopulating = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.denominationCheckComboBox.addEventHandler(ComboBox.ON_HIDDEN, this::onDenominationSelected);
    }


    @Override
    public void bindViewModel(
        Flow.Publisher<MultiSelectableItemList<DenominationItem>> denominationsList,
        ViewAction<List<DenominationItem>> selectDenominationsAction
    ) {
        this.selectDenominationsAction = selectDenominationsAction;
        denominationsList.subscribe(new GenericSubscriber<>(this::onDenominationsListChanged));
    }


    private void onDenominationsListChanged(MultiSelectableItemList<DenominationItem> denominationItems) {
        this.isPopulating = true;
        var items = this.denominationCheckComboBox.getItems();
        var checkModel = this.denominationCheckComboBox.getCheckModel();

        checkModel.clearChecks();
        items.clear();

        items.setAll(FXCollections.observableArrayList(denominationItems.getItems()));
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
