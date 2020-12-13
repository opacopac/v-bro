package com.tschanz.v_bro.app.presenter.version_aggregate;

import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class VersionAggregateResponse {
    private final VersionAggregateNodeResponse rootNode;


    public static VersionAggregateResponse fromDomain(VersionAggregate versionAggregate) {
        var rootNode = versionAggregate != null
            ? VersionAggregateNodeResponse.fromAggregateNode(versionAggregate.getRootNode())
            : null;

        return new VersionAggregateResponse(rootNode);
    }
}
