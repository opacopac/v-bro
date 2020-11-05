package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.VersionAggregateItem;

import java.util.concurrent.Flow;


public interface VersionAggregateView {
    void bindViewModel(Flow.Publisher<VersionAggregateItem> versionAggregate);
}
