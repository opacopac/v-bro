package com.tschanz.v_bro.dependencies.persistence.mock.service;

import com.tschanz.v_bro.common.testing.MockHelper;
import com.tschanz.v_bro.common.testing.MockReturnValue;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;


public class MockVersionAggregateService implements VersionAggregateService {
    public final MockHelper<RepoException> mockHelper = new MockHelper<>();
    public MockReturnValue<VersionAggregate> readVersionAggregateResults = new MockReturnValue<>("readVersionAggregate");


    @Override
    public VersionAggregate readVersionAggregate(String elementClass, String elementId, String versionId) throws RepoException {
        this.mockHelper.reportMethodCall("readVersionAggregate");
        this.mockHelper.checkThrowException();
        return this.readVersionAggregateResults.next();
    }
}
