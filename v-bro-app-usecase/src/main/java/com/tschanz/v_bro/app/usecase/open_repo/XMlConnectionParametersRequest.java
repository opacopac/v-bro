package com.tschanz.v_bro.app.usecase.open_repo;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class XMlConnectionParametersRequest extends ConnectionParametersRequest {
    public final String filename;


    public XMlConnectionParametersRequest(String filename) {
        super(RepoType.XML);
        this.filename = filename;
    }
}
