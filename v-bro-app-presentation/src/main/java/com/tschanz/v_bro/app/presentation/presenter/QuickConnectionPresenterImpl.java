package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.quick_connections.QuickConnectionItem;
import com.tschanz.v_bro.app.presenter.quick_connections.QuickConnectionsPresenter;
import com.tschanz.v_bro.app.presenter.quick_connections.QuickConnectionsResponse;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class QuickConnectionPresenterImpl implements QuickConnectionsPresenter {
    private final BehaviorSubject<List<QuickConnectionItem>> quickConnections;


    @Override
    public void present(@NonNull QuickConnectionsResponse response) {
        var newQuickConnection = QuickConnectionItem.fromResponse(response);

        this.quickConnections.next(newQuickConnection);
    }
}
