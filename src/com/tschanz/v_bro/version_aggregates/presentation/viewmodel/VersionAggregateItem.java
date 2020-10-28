package com.tschanz.v_bro.version_aggregates.presentation.viewmodel;


public class VersionAggregateItem {
    protected final AggregateNodeItem rootNode;


    public AggregateNodeItem getRootNode() { return rootNode; }


    public VersionAggregateItem(AggregateNodeItem rootNode) {
        this.rootNode = rootNode;
    }
}
