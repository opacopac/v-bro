package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.view.VersionAggregateView;
import com.tschanz.v_bro.app.presentation.viewmodel.version_aggregate.FieldAggregateNodeItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version_aggregate.VersionAggregateItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version_aggregate.VersionAggregateNodeItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.concurrent.Flow;

public class JfxVersionAggregateView implements VersionAggregateView {
    @FXML private TreeView<Object> aggregateTreeView;


    @Override
    public void bindViewModel(Flow.Publisher<VersionAggregateItem> versionAggregate) {
        versionAggregate.subscribe(new GenericSubscriber<>(this::onVersionAggregateChanged));
    }


    @FXML
    private void onMouseClicked(MouseEvent mouseEvent) {
        var selectedItem = this.aggregateTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null && mouseEvent.getButton() == MouseButton.SECONDARY) {
            var content = new ClipboardContent();
            content.putString(selectedItem.getValue().toString());
            Clipboard.getSystemClipboard().setContent(content);
        }
    }


    private void onVersionAggregateChanged(VersionAggregateItem versionAggregate) {
        if (versionAggregate != null) {
            var root = this.createTreeNode(versionAggregate.getRootNode());
            this.aggregateTreeView.setRoot(root);
        } else {
            this.aggregateTreeView.setRoot(null);
        }
    }


    private TreeItem<Object> createTreeNode(VersionAggregateNodeItem aggregateNode) {
        var node = new TreeItem<Object>(aggregateNode);

        aggregateNode.getFieldNodes().forEach(field -> node.getChildren().add(this.createFieldNode(field)));
        aggregateNode.getChildNodes().forEach(child -> node.getChildren().add(this.createTreeNode(child)));
        node.setExpanded(true);

        return node;
    }


    private TreeItem<Object> createFieldNode(FieldAggregateNodeItem fieldNode) {
        return new TreeItem<Object>(fieldNode);
    }
}
