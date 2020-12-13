package com.tschanz.v_bro.app.presentation.viewmodel.version_aggregate;

import com.tschanz.v_bro.app.presenter.version_aggregate.VersionAggregateNodeResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class VersionAggregateNodeItem {
    private final String nodeName;
    private final List<FieldAggregateNodeItem> fieldNodes;
    private final List<VersionAggregateNodeItem> childNodes;


    @Override
    public String toString() {
        return this.nodeName;
    }


    public static VersionAggregateNodeItem fromResponse(VersionAggregateNodeResponse node) {
        return new VersionAggregateNodeItem(
            node.getNodeName(),
            node.getFieldValues()
                .stream()
                .map(keyValue -> new FieldAggregateNodeItem(keyValue.key, keyValue.value))
                .collect(Collectors.toList()),
            node.getChildNodes()
                .stream()
                .map(VersionAggregateNodeItem::fromResponse)
                .collect(Collectors.toList())
        );
    }
}
