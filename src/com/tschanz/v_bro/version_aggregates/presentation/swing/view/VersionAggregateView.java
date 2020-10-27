package com.tschanz.v_bro.version_aggregates.presentation.swing.view;

import com.tschanz.v_bro.version_aggregates.presentation.swing.viewmodel.VersionAggregateItem;

import java.util.concurrent.Flow;


public interface VersionAggregateView {
    void bindVersionAggregate(Flow.Publisher<VersionAggregateItem> versionAggregate);
}
