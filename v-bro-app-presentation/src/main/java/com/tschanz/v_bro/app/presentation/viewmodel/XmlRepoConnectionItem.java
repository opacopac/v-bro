package com.tschanz.v_bro.app.presentation.viewmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.Getter;
import lombok.NonNull;


@Getter
public class XmlRepoConnectionItem extends RepoConnectionItem {
    private final String filename;


    public String getFilename() { return filename; }


    public XmlRepoConnectionItem(@NonNull String filename) {
        super(RepoType.XML);

        this.filename = filename;
    }
}
