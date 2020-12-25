package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.common.testing.SpyReturnValue;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;

import java.util.List;


public class SpyVersionService implements VersionService {
    public final SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    public SpyReturnValue<List<VersionData>> readVersionTimelineResult = new SpyReturnValue<>("readVersionTimeline");


    @Override
    public List<VersionData> readVersions(@NonNull ElementData element) throws RepoException {
        this.spyHelper.reportMethodCall("readVersionTimeline", element);
        this.spyHelper.checkThrowException();
        return this.readVersionTimelineResult.next();
    }
}
