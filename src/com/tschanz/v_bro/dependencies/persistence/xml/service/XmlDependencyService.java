package com.tschanz.v_bro.dependencies.persistence.xml.service;

import com.tschanz.v_bro.dependencies.domain.model.FwdDependency;
import com.tschanz.v_bro.dependencies.domain.service.DependencyService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import com.tschanz.v_bro.versions.persistence.xml.service.VersionParser;

import java.util.Collection;


public class XmlDependencyService implements DependencyService {
    private final XmlRepoService repoService;
    private final VersionParser parser;


    public XmlDependencyService(
        XmlRepoService repoService,
        VersionParser parser
    ) {
        this.repoService = repoService;
        this.parser = parser;
    }


    @Override
    public Collection<FwdDependency> readFwdDependencies(String elemenClass, String elementId, String versionId) throws RepoException {
        return null;
    }
}
