package com.tschanz.v_bro.app.usecase.common.responsemodel;

import com.tschanz.v_bro.common.KeyValue;

import java.util.List;


public class VersionAggregateNodeResponse {
    public final String nodeName;
    public final List<KeyValue> fieldValues;
    public final List<VersionAggregateNodeResponse> childNodes;


    public VersionAggregateNodeResponse(String nodeName, List<KeyValue> fieldValues, List<VersionAggregateNodeResponse> childNodes) {
        this.nodeName = nodeName;
        this.fieldValues = fieldValues;
        this.childNodes = childNodes;
    }
}
