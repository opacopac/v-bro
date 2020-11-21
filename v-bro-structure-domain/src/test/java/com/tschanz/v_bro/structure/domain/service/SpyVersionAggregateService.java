package com.tschanz.v_bro.structure.domain.service;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.common.testing.SpyReturnValue;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.structure.domain.service.VersionAggregateService;


public class SpyVersionAggregateService implements VersionAggregateService {
    public final SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    public SpyReturnValue<VersionAggregate> readVersionAggregateResults = new SpyReturnValue<>("readVersionAggregate");


    @Override
    public VersionAggregate readVersionAggregate(String elementClass, String elementId, String versionId) throws RepoException {
        this.spyHelper.reportMethodCall("readVersionAggregate");
        this.spyHelper.checkThrowException();
        return this.readVersionAggregateResults.next();
    }
}
