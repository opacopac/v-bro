package com.tschanz.v_bro.repo.persistence.xml.model;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoType;


public class XmlConnectionParameters implements ConnectionParameters {
    private final String filename;

    @Override
    public RepoType getRepoType() {
        return RepoType.XML;
    }
    public String getFilename() { return filename; }


    public XmlConnectionParameters(String filename) {
        this.filename = filename;
    }
}
