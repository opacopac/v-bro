package com.tschanz.v_bro.versioning.mock.service;

import com.tschanz.v_bro.common.testing.MockHelper;
import com.tschanz.v_bro.common.testing.MockReturnValue;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.versioning.domain.model.VersionAggregate;
import com.tschanz.v_bro.versioning.domain.model.VersionInfo;
import com.tschanz.v_bro.versioning.domain.service.VersionService;

import java.util.Collection;
import java.util.List;


public class MockVersionService implements VersionService {
    public final MockHelper<RepoException> mockHelper = new MockHelper<>();
    public MockReturnValue<List<VersionInfo>> readVersionTimelineResult = new MockReturnValue<>("readVersionTimeline");
    public MockReturnValue<VersionAggregate> readVersionAggregateResults = new MockReturnValue<>("readVersionAggregate");


    @Override
    public Collection<VersionInfo> readVersionTimeline(String elementName, String elementId) throws RepoException {
        this.mockHelper.reportMethodCall("readVersionTimeline");
        this.mockHelper.checkThrowException();
        return this.readVersionTimelineResult.next();
    }


    @Override
    public VersionAggregate readVersionAggregate(String elementName, String elementId, String versionId) throws RepoException {
        this.mockHelper.reportMethodCall("readVersionAggregate");
        this.mockHelper.checkThrowException();
        return this.readVersionAggregateResults.next();
    }
}
