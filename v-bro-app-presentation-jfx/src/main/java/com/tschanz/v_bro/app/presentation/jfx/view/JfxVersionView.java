package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.controller.VersionController;
import com.tschanz.v_bro.app.presentation.view.VersionsView;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;


public class JfxVersionView implements VersionsView {
    @FXML private VBox versionTimlinesVBox;
    private VersionFilterItem effectiveVersionFilter;
    private VersionController versionController;


    @Override
    public void bindViewModel(
        Flow.Publisher<SelectableItemList<VersionItem>> versionList,
        Flow.Publisher<VersionFilterItem> effectiveVersionFilter,
        VersionController versionController
    ) {
        this.versionController = versionController;
        effectiveVersionFilter.subscribe(new GenericSubscriber<>(this::onVersionFilterChanged));
        versionList.subscribe(new GenericSubscriber<>(this::onVersionListChanged));
    }


    private void onVersionFilterChanged(VersionFilterItem versionFilter) {
        this.effectiveVersionFilter = versionFilter;
    }


    @SneakyThrows
    private void onVersionListChanged(SelectableItemList<VersionItem> versionList) {
        this.versionTimlinesVBox.getChildren().clear();

        if (versionList != null) {
            var versionsByStatus = this.groupVersionsByStatus(versionList);
            for (var versions : versionsByStatus) {
                var viewUrl = getClass().getClassLoader().getResource("VersionTimeline.fxml");
                var fxmlLoader = new FXMLLoader(viewUrl);
                Node node = fxmlLoader.load();

                JfxVersionTimeline versionTimeline = fxmlLoader.getController();
                versionTimeline.bindViewModel(
                    versions,
                    this.effectiveVersionFilter,
                    this.versionController
                );
                this.versionTimlinesVBox.getChildren().add(node);
            }
        }
    }


    private List<SelectableItemList<VersionItem>> groupVersionsByStatus(SelectableItemList<VersionItem> versionList) {
        List<SelectableItemList<VersionItem>> versionsByPflegestatus = new ArrayList<>();

        for (var status: Pflegestatus.values()) {
            var versions = versionList.getItems()
                .stream()
                .filter(v -> v.getPflegestatus().equals(status))
                .collect(Collectors.toList());
            var selectedVersionId = versions.contains(versionList.getSelectedItem()) ? versionList.getSelectedItem().getId() : null;
            if (versions.size() > 0) {
                versionsByPflegestatus.add(new SelectableItemList<VersionItem>(versions, selectedVersionId));
            }
        }

        return versionsByPflegestatus;
    }
}
