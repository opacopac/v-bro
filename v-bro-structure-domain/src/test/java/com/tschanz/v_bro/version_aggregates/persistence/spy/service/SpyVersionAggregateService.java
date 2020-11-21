package com.tschanz.v_bro.version_aggregates.persistence.spy.service;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.common.testing.SpyReturnValue;
import com.tschanz.v_bro.dependencies.domain.model.FwdDependency;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;
import com.tschanz.v_bro.versions.domain.model.VersionData;

import java.util.List;


public class SpyVersionAggregateService implements VersionAggregateService {
    public final SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    public SpyReturnValue<List<VersionData>> readVersionTimelineResult = new SpyReturnValue<>("readVersionTimeline");
    public SpyReturnValue<List<FwdDependency>> readFwdDependenciesResults = new SpyReturnValue<>("readFwdDependencies");
    public SpyReturnValue<VersionAggregate> readVersionAggregateResults = new SpyReturnValue<>("readVersionAggregate");


    @Override
    public VersionAggregate readVersionAggregate(String elementClass, String elementId, String versionId) throws RepoException {
        this.spyHelper.reportMethodCall("readVersionAggregate");
        this.spyHelper.checkThrowException();
        return this.readVersionAggregateResults.next();
    }
}
