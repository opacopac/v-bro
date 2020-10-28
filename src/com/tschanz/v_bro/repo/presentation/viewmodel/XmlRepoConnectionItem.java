package com.tschanz.v_bro.repo.presentation.viewmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;


public class XmlRepoConnectionItem extends RepoConnectionItem {
    private final String filename;


    public String getFilename() { return filename; }


    public XmlRepoConnectionItem(String filename) {
        super(RepoType.XML);

        if (filename == null) {
            throw new IllegalArgumentException("filename must not be null");
        }

        this.filename = filename;
    }
}
