package com.tschanz.v_bro.app.usecase.common.converter;

import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionAggregateNodeResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionAggregateResponse;
import com.tschanz.v_bro.structure.domain.model.AggregateNode;
import com.tschanz.v_bro.structure.domain.model.VersionAggregate;

import java.util.stream.Collectors;


public class VersionAggregateConverter {
    public static VersionAggregateResponse toResponse(VersionAggregate versionAggregate) {
        if (versionAggregate == null) {
            return null;
        } else {
            return new VersionAggregateResponse(
                toResponse(versionAggregate.getRootNode())
            );
        }
    }


    public static VersionAggregateNodeResponse toResponse(AggregateNode node) {
        return new VersionAggregateNodeResponse(
            node.getNodeName(),
            node.getFieldValues(),
            node.getChildNodes()
                .stream()
                .map(VersionAggregateConverter::toResponse)
                .collect(Collectors.toList())
        );
    }
}
