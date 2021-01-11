package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.controller.VersionFilterController;
import com.tschanz.v_bro.app.presentation.view.VersionFilterView;
import com.tschanz.v_bro.app.presentation.viewmodel.version.PflegestatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;


public class JfxVersionFilterView implements VersionFilterView, Initializable {
    @FXML private DatePicker minVonDatePicker;
    @FXML private DatePicker maxBisDatePicker;
    @FXML private ComboBox<PflegestatusItem> pflegestatusComboBox;
    private VersionFilterController versionFilterController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO: set values & selection from app state
        this.pflegestatusComboBox.getItems().addAll(
            List.of(
                new PflegestatusItem(Pflegestatus.IN_ARBEIT),
                new PflegestatusItem(Pflegestatus.TEST),
                new PflegestatusItem(Pflegestatus.ABNAHME),
                new PflegestatusItem(Pflegestatus.PRODUKTIV)
            )
        );
    }


    @Override
    public void bindViewModel(
        Flow.Publisher<VersionFilterItem> versionFilter,
        VersionFilterController versionFilterController
    ) {
        this.versionFilterController = versionFilterController;
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
    private void onFilterSelected(ActionEvent actionEvent) {
        new Thread(() -> this.versionFilterController.selectVersionFilter(this.getVersionFilterItem())).start();
    }


    private VersionFilterItem getVersionFilterItem() {
        return new VersionFilterItem(
            this.minVonDatePicker.getValue(),
            this.maxBisDatePicker.getValue(),
            this.pflegestatusComboBox.getValue().getPflegestatus()
        );
    }
}
