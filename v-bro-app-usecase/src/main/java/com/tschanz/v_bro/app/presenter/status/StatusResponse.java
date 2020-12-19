package com.tschanz.v_bro.app.presenter.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class StatusResponse {
    private final String message;
    private final boolean isError;
    private final boolean isWaiting;
}
