package com.tschanz.v_bro.version_aggregates.presentation.viewmodel;

import java.util.List;


public class AggregateNodeItem {
    private final String nodeName;
    private final List<FieldAggregateNodeItem> fieldNodes;
    private final List<AggregateNodeItem> childNodes;


    public String getNodeName() { return nodeName; }
    public List<FieldAggregateNodeItem> getFieldNodes() { return fieldNodes; }
    public List<AggregateNodeItem> getChildNodes() { return childNodes; }


    public AggregateNodeItem(String nodeName, List<FieldAggregateNodeItem> fieldNodes, List<AggregateNodeItem> childNodes) {
        this.nodeName = nodeName;
        this.fieldNodes = fieldNodes;
        this.childNodes = childNodes;
    }


    @Override
    public String toString() {
        return this.nodeName;
    }
}
