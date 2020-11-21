package com.tschanz.v_bro.repo.persistence.model;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class XmlConnectionParameters implements ConnectionParameters {
    @Getter private final String filename;


    @Override
    public RepoType getRepoType() {
        return RepoType.XML;
    }
}
