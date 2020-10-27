package com.tschanz.v_bro.version_aggregates.domain.model;

import com.tschanz.v_bro.common.KeyValue;

import java.util.List;


public class AggregateNode {
    private final String nodeName;
    private final List<KeyValue> fieldValues;
    private final List<AggregateNode> childNodes;


    public String getNodeName() { return nodeName; }
    public List<KeyValue> getFieldValues() { return fieldValues; }
    public List<AggregateNode> getChildNodes() { return childNodes; }


    public AggregateNode(String nodeName, List<KeyValue> fieldValues, List<AggregateNode> childNodes) {
        if (nodeName == null || nodeName.isEmpty()) {
            throw new IllegalArgumentException("nodeName must not be null or empty");
        }

        if (fieldValues == null) {
            throw new IllegalArgumentException("fieldValues must not be null");
        }

        if (childNodes == null) {
            throw new IllegalArgumentException("childNodes must not be null");
        }

        this.nodeName = nodeName;
        this.fieldValues = fieldValues;
        this.childNodes = childNodes;
    }
}
