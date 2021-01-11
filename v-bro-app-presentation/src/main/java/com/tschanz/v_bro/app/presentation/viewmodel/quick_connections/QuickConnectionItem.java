package com.tschanz.v_bro.app.presentation.viewmodel.quick_connections;

import com.tschanz.v_bro.app.presenter.quick_connections.QuickConnectionsResponse;
import com.tschanz.v_bro.app.presenter.quick_connections.QuickConnectionsResponseItem;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class QuickConnectionItem {
    private final String key;
    private final RepoType repoType;
    private final String url;
    private final String user;
    private final String password;
    private final String filename;


    public static List<QuickConnectionItem> fromResponse(QuickConnectionsResponse response) {
        return response.getQuickConnections()
            .stream()
            .map(QuickConnectionItem::fromResponse)
            .collect(Collectors.toList());
    }


    public static QuickConnectionItem fromResponse(QuickConnectionsResponseItem responseItem) {
        return new QuickConnectionItem(
            responseItem.getKey(),
            responseItem.getRepoType(),
            responseItem.getUrl(),
            responseItem.getUser(),
            responseItem.getPassword(),
            responseItem.getFilename()
        );
    }


    @Override
    public String toString() {
        return this.key;
    }
}
