package com.tschanz.v_bro.app.presenter.quick_connections;

import com.tschanz.v_bro.repo.domain.model.QuickConnection;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class QuickConnectionsResponse {
    @NonNull private final List<QuickConnectionsResponseItem> quickConnections;


    public static QuickConnectionsResponse fromDomain(List<QuickConnection> quickConnections) {
        var qcList = quickConnections
            .stream()
            .map(QuickConnectionsResponseItem::fromDomain)
            .collect(Collectors.toList());

        return new QuickConnectionsResponse(qcList);
    }
}
