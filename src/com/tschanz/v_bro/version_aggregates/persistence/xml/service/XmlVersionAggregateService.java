package com.tschanz.v_bro.version_aggregates.persistence.xml.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;
import com.tschanz.v_bro.versions.persistence.xml.service.VersionParser;


public class XmlVersionAggregateService implements VersionAggregateService {
    private final XmlRepoService repoService;
    private final VersionParser parser;


    public XmlVersionAggregateService(
        XmlRepoService repoService,
        VersionParser parser
    ) {
        this.repoService = repoService;
        this.parser = parser;
    }


    @Override
    public VersionAggregate readVersionAggregate(String elementClass, String elementId, String versionId) throws RepoException {
        return null;
    }
}
