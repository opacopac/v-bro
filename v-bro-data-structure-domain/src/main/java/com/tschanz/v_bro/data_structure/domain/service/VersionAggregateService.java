package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;


public interface VersionAggregateService {
    VersionAggregate readVersionAggregate(@NonNull VersionData version) throws RepoException;
}
