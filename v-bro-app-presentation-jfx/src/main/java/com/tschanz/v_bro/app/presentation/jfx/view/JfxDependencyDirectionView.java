package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.controller.DependencyDirectionController;
import com.tschanz.v_bro.app.presentation.view.DependencyDirectionView;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.DependencyDirectionItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;

import java.util.concurrent.Flow;


public class JfxDependencyDirectionView implements DependencyDirectionView {
    @FXML private RadioButton fwdDependencyRadio;
    @FXML private RadioButton bwdDependencyRadio;
    private DependencyDirectionController dependencyDirectionController;


    @Override
    public void bindViewModel(
        Flow.Publisher<DependencyDirectionItem> dependencyDirection,
        DependencyDirectionController dependencyDirectionController
    ) {
        this.dependencyDirectionController = dependencyDirectionController;
        dependencyDirection.subscribe(new GenericSubscriber<>(this::onDependencyDirectionChanged));
    }


    private void onDependencyDirectionChanged(DependencyDirectionItem dependencyFilter) {
        if (dependencyFilter == null) {
            return;
        } else if (dependencyFilter.isFwd()) {
            this.fwdDependencyRadio.setSelected(true);
            this.bwdDependencyRadio.setSelected(false);
        } else {
            this.fwdDependencyRadio.setSelected(false);
            this.bwdDependencyRadio.setSelected(true);
        }

        if (this.dependencyDirectionController != null) {
            new Thread(() -> this.dependencyDirectionController.selectDependencyDirection(dependencyFilter)).start();
        }
    }


    @FXML private void onFwdDependencySelected(ActionEvent actionEvent) {
        var dependencyFilter = new DependencyDirectionItem(true);
        new Thread(() -> this.dependencyDirectionController.selectDependencyDirection(dependencyFilter)).start();
    }


    @FXML private void onBwdDependencySelected(ActionEvent actionEvent) {
        var dependencyFilter = new DependencyDirectionItem(false);
        new Thread(() -> this.dependencyDirectionController.selectDependencyDirection(dependencyFilter)).start();
    }
}
