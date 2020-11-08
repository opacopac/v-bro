package com.tschanz.v_bro.version_aggregates.persistence.jdbc.service;

import com.tschanz.v_bro.common.cache.Cache;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;

import java.util.logging.Logger;


public class JdbcVersionAggregateServiceCache implements VersionAggregateService {
    private final Logger logger = Logger.getLogger(JdbcVersionAggregateServiceCache.class.getName());
    private final JdbcVersionAggregateService versionAggregateService;
    private final Cache<VersionAggregate> cache;


    public JdbcVersionAggregateServiceCache(
        JdbcVersionAggregateService versionAggregateService,
        Cache<VersionAggregate> cache
    ) {
        this.versionAggregateService = versionAggregateService;
        this.cache = cache;
    }


    @Override
    public VersionAggregate readVersionAggregate(String elementClass, String elementId, String versionId) throws RepoException {
        String key = elementClass + "/" + elementId + "/" + versionId;
        VersionAggregate cachedResult = this.cache.getItem(key);
        if (cachedResult != null) {
            this.logger.info("serving from cache: version aggregate for class/elementId/versionId " + key);
            return cachedResult;
        } else {
            VersionAggregate aggregate = this.versionAggregateService.readVersionAggregate(elementClass, elementId, versionId);
            this.cache.addItem(key, aggregate);
            return aggregate;
        }
    }
}
