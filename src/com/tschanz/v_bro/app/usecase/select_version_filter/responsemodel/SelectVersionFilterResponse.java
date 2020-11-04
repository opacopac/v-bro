package com.tschanz.v_bro.app.usecase.select_version_filter.responsemodel;

import com.tschanz.v_bro.app.usecase.common.responsemodel.UseCaseResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;

import java.util.List;


public class SelectVersionFilterResponse extends UseCaseResponse {
    public final List<VersionResponse> versions;


    public SelectVersionFilterResponse(
        List<VersionResponse> versions,
        String message,
        boolean isError
    ) {
        super(message, isError);
        this.versions = versions;
    }
}
