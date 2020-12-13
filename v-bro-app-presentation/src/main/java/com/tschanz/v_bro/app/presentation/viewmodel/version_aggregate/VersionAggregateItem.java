package com.tschanz.v_bro.app.presentation.viewmodel.version_aggregate;

import com.tschanz.v_bro.app.presenter.version_aggregate.VersionAggregateResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class VersionAggregateItem {
    private final VersionAggregateNodeItem rootNode;


    public static VersionAggregateItem fromResponse(VersionAggregateResponse versionAggregate) {
        var rootNode = versionAggregate.getRootNode() != null
            ? versionAggregate.getRootNode()
            : null;

        if (rootNode == null) {
            return null;
        } else {
            return new VersionAggregateItem(
                VersionAggregateNodeItem.fromResponse(rootNode)
            );
        }
    }
}
