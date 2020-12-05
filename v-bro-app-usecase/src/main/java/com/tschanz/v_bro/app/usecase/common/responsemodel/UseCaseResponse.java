package com.tschanz.v_bro.app.usecase.common.responsemodel;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class UseCaseResponse {
    public final String message;
    public final boolean isError;
}
