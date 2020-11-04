package com.tschanz.v_bro.app.usecase.common.responsemodel;


public abstract class UseCaseResponse {
    public final String message;
    public final boolean isError;


    public UseCaseResponse(String message, boolean isError) {
        this.message = message;
        this.isError = isError;
    }
}
