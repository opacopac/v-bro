package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.common.cache.Cache;
import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.VersionAggregateService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class JdbcVersionAggregateServiceCache implements VersionAggregateService {
    private final JdbcVersionAggregateService versionAggregateService;
    private final Cache<VersionAggregate> cache;


    @Override
    public VersionAggregate readVersionAggregate(@NonNull VersionData version) throws RepoException {
        String key = version.getElement().getElementClass().getName() + "/"
            + version.getElement().getId() + "/"
            + version.getId();
        VersionAggregate cachedResult = this.cache.getItem(key);
        if (cachedResult != null) {
            log.info("serving from cache: version aggregate for class/elementId/versionId " + key);
            return cachedResult;
        } else {
            VersionAggregate aggregate = this.versionAggregateService.readVersionAggregate(version);
            this.cache.addItem(key, aggregate);
            return aggregate;
        }
    }
}
