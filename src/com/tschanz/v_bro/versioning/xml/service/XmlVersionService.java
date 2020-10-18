package com.tschanz.v_bro.versioning.xml.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.xml.service.XmlRepoService;
import com.tschanz.v_bro.versioning.domain.model.VersionData;
import com.tschanz.v_bro.versioning.domain.service.VersionService;
import com.tschanz.v_bro.versioning.xml.model.XmlVersionData;

import java.util.ArrayList;
import java.util.Collection;


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
    public Collection<VersionData> readVersions(String elementName, String elementId) throws RepoException {
        if (!this.repoService.isConnected()) {
            throw new RepoException("Repo not connected!");
        }

        Collection<XmlVersionData> versionDataList = this.parser.readVersions(
            this.repoService,
            elementName,
            elementId
        );

        return new ArrayList<>(versionDataList);
    }
}
