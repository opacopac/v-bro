package com.tschanz.v_bro.versions.persistence.spy.service;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.common.testing.SpyReturnValue;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.versions.domain.model.VersionData;
import com.tschanz.v_bro.versions.domain.service.VersionService;

import java.util.List;


public class SpyVersionService implements VersionService {
    public final SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    public SpyReturnValue<List<VersionData>> readVersionTimelineResult = new SpyReturnValue<>("readVersionTimeline");


    @Override
    public List<VersionData> readVersionTimeline(String elementClass, String elementId) throws RepoException {
        this.spyHelper.reportMethodCall("readVersionTimeline");
        this.spyHelper.checkThrowException();
        return this.readVersionTimelineResult.next();
    }
}
