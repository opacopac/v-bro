package com.tschanz.v_bro.app.usecase.common.responsemodel;


public class VersionAggregateResponse {
    public final VersionAggregateNodeResponse rootNode;


    public VersionAggregateResponse(VersionAggregateNodeResponse rootNode) {
        this.rootNode = rootNode;
    }
}