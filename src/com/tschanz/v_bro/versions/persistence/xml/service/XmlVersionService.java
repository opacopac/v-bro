package com.tschanz.v_bro.versions.persistence.xml.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;
import com.tschanz.v_bro.versions.domain.service.VersionService;
import com.tschanz.v_bro.versions.persistence.xml.model.XmlVersionInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class XmlVersionService implements VersionService {
    private final XmlRepoService repoService;
    private final VersionParser parser;


    public XmlVersionService(
        XmlRepoService repoService,
        VersionParser parser
    ) {
        this.repoService = repoService;
        this.parser = parser;
    }


    @Override
    public List<VersionInfo> readVersionTimeline(String elementClass, String elementId) throws RepoException {
        if (!this.repoService.isConnected()) {
            throw new RepoException("Repo not connected!");
        }

        Collection<XmlVersionInfo> versionDataList = this.parser.readVersions(
            this.repoService,
            elementClass,
            elementId
        );

        return new ArrayList<>(versionDataList);
    }
}
