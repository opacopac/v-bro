package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.presentation.view.VersionAggregateView;
import com.tschanz.v_bro.app.presentation.viewmodel.AggregateNodeItem;
import com.tschanz.v_bro.app.presentation.viewmodel.FieldAggregateNodeItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionAggregateItem;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.concurrent.Flow;


public class VersionAggregateTree extends JPanel implements VersionAggregateView {


    public VersionAggregateTree() {
    }


    @Override
    public void bindVersionAggregate(Flow.Publisher<VersionAggregateItem> versionAggregate) {
        versionAggregate.subscribe(new GenericSubscriber<>(this::onVersionAggregateChanged));
    }


    private void onVersionAggregateChanged(VersionAggregateItem versionAggregate) {
        this.removeAll();

        if (versionAggregate != null) {
            DefaultMutableTreeNode root = this.createTreeNode(versionAggregate.getRootNode());
            JTree aggregateTree = new JTree(root);
            JScrollPane scrollPane = new JScrollPane(aggregateTree);
            scrollPane.setPreferredSize(new Dimension(400, 500));
            this.add(scrollPane);
            this.expandAllNodes(aggregateTree);
        }

        this.repaint();
        this.revalidate();
    }


    private DefaultMutableTreeNode createTreeNode(AggregateNodeItem aggregateNode) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(aggregateNode, true);

        aggregateNode.getFieldNodes().forEach(field -> node.add(this.createFieldNode(field)));
        aggregateNode.getChildNodes().forEach(child -> node.add(this.createTreeNode(child)));

        return node;
    }


    private DefaultMutableTreeNode createFieldNode(FieldAggregateNodeItem fieldNode) {
        return new DefaultMutableTreeNode(fieldNode);
    }


    private void expandAllNodes(JTree tree) {
        if (tree == null) {
            return;
        }

        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
}
