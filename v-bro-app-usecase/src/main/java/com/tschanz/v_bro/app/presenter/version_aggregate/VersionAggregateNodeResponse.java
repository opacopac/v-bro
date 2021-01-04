package com.tschanz.v_bro.app.presenter.version_aggregate;

import com.tschanz.v_bro.common.types.KeyValue;
import com.tschanz.v_bro.data_structure.domain.model.AggregateNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class VersionAggregateNodeResponse {
    private final String nodeName;
    private final List<KeyValue> fieldValues;
    private final List<VersionAggregateNodeResponse> childNodes;


    public static VersionAggregateNodeResponse fromAggregateNode(AggregateNode node) {
        return new VersionAggregateNodeResponse(
            node.getNodeName(),
            node.getFieldValues(),
            node.getChildNodes()
                .stream()
                .map(VersionAggregateNodeResponse::fromAggregateNode)
                .collect(Collectors.toList())
        );
    }
}
