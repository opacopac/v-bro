package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.actions.SelectVersionFilterAction;
import com.tschanz.v_bro.app.presentation.view.VersionFilterView;
import com.tschanz.v_bro.app.presentation.viewmodel.PflegestatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.util.concurrent.Flow;


public class JfxVersionFilterView implements VersionFilterView {
    @FXML private DatePicker minVonDatePicker;
    @FXML private DatePicker maxBisDatePicker;
    @FXML private ComboBox<PflegestatusItem> pflegestatusComboBox;
    private SelectVersionFilterAction selectVersionFilterAction;


    @Override
    public void bindViewModel(
        Flow.Publisher<VersionFilterItem> versionFilter,
        SelectVersionFilterAction selectVersionFilterAction
    ) {
        this.selectVersionFilterAction = selectVersionFilterAction;
        versionFilter.subscribe(new GenericSubscriber<>(this::onVersionFilterChanged));
    }


    private void onVersionFilterChanged(VersionFilterItem versionFilter) {
        if (versionFilter == null) {
            return;
        }

        this.minVonDatePicker.setValue(versionFilter.getMinGueltigVon());
        this.maxBisDatePicker.setValue(versionFilter.getMaxGueltigBis());
        this.pflegestatusComboBox.setValue(new PflegestatusItem(versionFilter.getMinPflegestatus()));
    }


    @FXML
    private void onMinVonSelected(ActionEvent actionEvent) {
        this.selectVersionFilterAction.next(
            this.getVersionFilterItem()
        );
    }


    @FXML
    private void onMaxBisSelected(ActionEvent actionEvent) {
        this.selectVersionFilterAction.next(
            this.getVersionFilterItem()
        );
    }


    @FXML
    private void onPflegestatusSelected(ActionEvent actionEvent) {
        this.selectVersionFilterAction.next(
            this.getVersionFilterItem()
        );
    }


    private VersionFilterItem getVersionFilterItem() {
        return new VersionFilterItem(
            this.minVonDatePicker.getValue(),
            this.maxBisDatePicker.getValue(),
            this.pflegestatusComboBox.getValue().getPflegestatus()
        );
    }
}
