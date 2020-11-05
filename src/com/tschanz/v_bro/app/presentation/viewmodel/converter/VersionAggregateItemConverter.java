package com.tschanz.v_bro.app.presentation.viewmodel.converter;

import com.tschanz.v_bro.app.presentation.viewmodel.AggregateNodeItem;
import com.tschanz.v_bro.app.presentation.viewmodel.FieldAggregateNodeItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionAggregateItem;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionAggregateNodeResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionAggregateResponse;

import java.util.stream.Collectors;


public class VersionAggregateItemConverter {
    public static VersionAggregateItem fromResponse(VersionAggregateResponse versionAggregate) {
        return new VersionAggregateItem(
            fromResponse(versionAggregate.rootNode)
        );
    }


    public static AggregateNodeItem fromResponse(VersionAggregateNodeResponse node) {
        return new AggregateNodeItem(
            node.nodeName,
            node.fieldValues
                .stream()
                .map(keyValue -> new FieldAggregateNodeItem(keyValue.key, keyValue.value))
                .collect(Collectors.toList()),
            node.childNodes
                .stream()
                .map(VersionAggregateItemConverter::fromResponse)
                .collect(Collectors.toList())
        );
    }
}
