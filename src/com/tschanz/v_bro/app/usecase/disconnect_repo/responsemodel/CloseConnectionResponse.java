package com.tschanz.v_bro.app.usecase.disconnect_repo.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.UseCaseResponse;


public class CloseConnectionResponse extends UseCaseResponse {
    public CloseConnectionResponse(String message, boolean isError) {
        super(message, isError);
    }
}
