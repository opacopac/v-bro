package com.tschanz.v_bro.app.usecase.select_element.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.UseCaseResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;

import java.util.List;


public class SelectElementResponse extends UseCaseResponse {
    public final List<VersionResponse> versions;


    public SelectElementResponse(
        List<VersionResponse> versions,
        String message,
        boolean isError
    ) {
        super(message, isError);
        this.versions = versions;
    }
}
