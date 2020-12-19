package com.tschanz.v_bro.app.presentation.viewmodel.status;

import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class StatusItem {
    private final String message;
    private final boolean isError;
    private final boolean isWaiting;


    public static StatusItem fromResponse(StatusResponse response) {
        return new StatusItem(
            response.getMessage(),
            response.isError(),
            response.isWaiting()
        );
    }
}
