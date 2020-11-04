package com.tschanz.v_bro.app.usecase.select_version.responsemodel;


public class VersionAggregateResponse {
    public final VersionAggregateNodeResponse rootNode;


    public VersionAggregateResponse(VersionAggregateNodeResponse rootNode) {
        this.rootNode = rootNode;
    }
}