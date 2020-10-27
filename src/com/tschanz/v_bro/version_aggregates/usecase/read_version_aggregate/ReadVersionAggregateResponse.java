package com.tschanz.v_bro.version_aggregates.usecase.read_version_aggregate;

import com.tschanz.v_bro.common.KeyValue;

import java.util.List;


public class ReadVersionAggregateResponse {
    public final VersionAggregateInfo versionAggregateInfo;


    public ReadVersionAggregateResponse(VersionAggregateInfo versionAggregateInfo) {
        this.versionAggregateInfo = versionAggregateInfo;
    }


    public static class VersionAggregateInfo {
        public final AggregateNodeInfo rootNode;


        public VersionAggregateInfo(AggregateNodeInfo rootNode) {
            this.rootNode = rootNode;
        }
    }


    public static class AggregateNodeInfo {
        public final String nodeName;
        public final List<KeyValue> fieldValues;
        public final List<AggregateNodeInfo> childNodes;


        public AggregateNodeInfo(String nodeName, List<KeyValue> fieldValues, List<AggregateNodeInfo> childNodes) {
            this.nodeName = nodeName;
            this.fieldValues = fieldValues;
            this.childNodes = childNodes;
        }
    }
}
