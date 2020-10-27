package com.tschanz.v_bro.versions.persistence.mock.service;

import com.tschanz.v_bro.common.testing.MockHelper;
import com.tschanz.v_bro.common.testing.MockReturnValue;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;
import com.tschanz.v_bro.versions.domain.service.VersionService;

import java.util.List;


public class MockVersionService implements VersionService {
    public final MockHelper<RepoException> mockHelper = new MockHelper<>();
    public MockReturnValue<List<VersionInfo>> readVersionTimelineResult = new MockReturnValue<>("readVersionTimeline");


    @Override
    public List<VersionInfo> readVersionTimeline(String elementClass, String elementId) throws RepoException {
        this.mockHelper.reportMethodCall("readVersionTimeline");
        this.mockHelper.checkThrowException();
        return this.readVersionTimelineResult.next();
    }
}
