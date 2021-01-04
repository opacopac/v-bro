package com.tschanz.v_bro.app.presenter.repo_connection;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class XmlConnectionResponse extends RepoConnectionResponse {
    public final String filename;


    public XmlConnectionResponse(String filename) {
        super(RepoType.XML);
        this.filename = filename;
    }
}