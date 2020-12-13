package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ViewAction;
import com.tschanz.v_bro.app.presentation.view.DependencyListView;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.ElementVersionVector;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.FwdDependencyItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.Flow;


public class JfxDependencyListView implements DependencyListView {
    @FXML private VBox depListVBox;
    private Flow.Publisher<VersionFilterItem> effectiveVersionFilter;
    private ViewAction<ElementVersionVector> selectDependencyVersionAction;


    @Override
    public void bindViewModel(
        Flow.Publisher<List<FwdDependencyItem>> fwdDependencyList,
        Flow.Publisher<VersionFilterItem> versionFilter,
        ViewAction<ElementVersionVector> selectDependencyVersionAction
    ) {
        this.effectiveVersionFilter = versionFilter;
        this.selectDependencyVersionAction = selectDependencyVersionAction;
        fwdDependencyList.subscribe(new GenericSubscriber<>(this::onFwdDependenciesChanged));
    }


    @SneakyThrows
    private void onFwdDependenciesChanged(List<FwdDependencyItem> fwdDependencyList) {
        this.depListVBox.getChildren().clear();

        if (fwdDependencyList != null) {
            for (FwdDependencyItem dependency : fwdDependencyList) {
                var viewUrl = getClass().getClassLoader().getResource("DependencyListEntryView.fxml");
                var fxmlLoader = new FXMLLoader(viewUrl);
                Node node = fxmlLoader.load();

                JfxDependencyListEntryView dependencyListEntry = fxmlLoader.getController();
                dependencyListEntry.bindViewModel(
                    dependency,
                    this.effectiveVersionFilter,
                    this.selectDependencyVersionAction
                );
                this.depListVBox.getChildren().add(node);
            }
        }
    }
}
