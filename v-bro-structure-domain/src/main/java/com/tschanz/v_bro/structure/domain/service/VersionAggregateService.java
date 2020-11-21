package com.tschanz.v_bro.structure.domain.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.structure.domain.model.VersionAggregate;


public interface VersionAggregateService {
    VersionAggregate readVersionAggregate(String elementClass, String elementId, String versionId) throws RepoException;
}
