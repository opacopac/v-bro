package com.tschanz.v_bro.app.usecase.connect_repo.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.UseCaseResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementClassResponse;

import java.util.List;


public class OpenConnectionResponse extends UseCaseResponse {
    public final RepoConnectionResponse repoConnection;
    public final List<ElementClassResponse> elementClasses;


    public OpenConnectionResponse(
        RepoConnectionResponse repoConnection,
        List<ElementClassResponse> elementClasses,
        String message,
        boolean isError
    ) {
        super(message, isError);
        this.repoConnection = repoConnection;
        this.elementClasses = elementClasses;
    }
}
