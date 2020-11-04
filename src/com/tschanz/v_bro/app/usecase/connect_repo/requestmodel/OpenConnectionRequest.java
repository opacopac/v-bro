package com.tschanz.v_bro.app.usecase.connect_repo.requestmodel;

public class OpenConnectionRequest {
    public final ConnectionParametersRequest connectionParameters;


    public OpenConnectionRequest(ConnectionParametersRequest connectionParameters) {
        this.connectionParameters = connectionParameters;
    }
}

