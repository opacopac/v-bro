package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.repo.domain.model.RepoException;


public interface VersionAggregateService {
    VersionAggregate readVersionAggregate(String elementClass, String elementId, String versionId) throws RepoException;
}
