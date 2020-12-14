package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.controller.DependencyFilterController;
import com.tschanz.v_bro.app.presentation.view.DependencyFilterView;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.DependencyFilterItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;

import java.util.concurrent.Flow;


public class JfxDependencyFilterView implements DependencyFilterView {
    @FXML private RadioButton fwdDependencyRadio;
    @FXML private RadioButton bwdDependencyRadio;
    private DependencyFilterController dependencyFilterController;


    @Override
    public void bindViewModel(
        Flow.Publisher<DependencyFilterItem> dependencyFilter,
        DependencyFilterController dependencyFilterController
    ) {
        this.dependencyFilterController = dependencyFilterController;
        dependencyFilter.subscribe(new GenericSubscriber<>(this::onInitialFilterChanged));
    }


    private void onInitialFilterChanged(DependencyFilterItem dependencyFilter) {
        if (dependencyFilter == null) {
            return;
        } else if (dependencyFilter.isFwd()) {
            this.fwdDependencyRadio.setSelected(true);
            this.bwdDependencyRadio.setSelected(false);
        } else {
            this.fwdDependencyRadio.setSelected(false);
            this.bwdDependencyRadio.setSelected(true);
        }

        if (this.dependencyFilterController != null) {
            this.dependencyFilterController.onDependencyFilterSelected(dependencyFilter);
        }
    }


    @FXML private void onFwdDependencySelected(ActionEvent actionEvent) {
        this.dependencyFilterController.onDependencyFilterSelected(
            new DependencyFilterItem(true)
        );
    }


    @FXML private void onBwdDependencySelected(ActionEvent actionEvent) {
        this.dependencyFilterController.onDependencyFilterSelected(
            new DependencyFilterItem(false)
        );
    }
}
