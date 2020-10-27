package com.tschanz.v_bro.version_aggregates.domain.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;


public interface VersionAggregateService {
    VersionAggregate readVersionAggregate(String elementClass, String elementId, String versionId) throws RepoException;
}
