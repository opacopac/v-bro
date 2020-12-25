package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.common.testing.SpyReturnValue;
import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;


public class SpyVersionAggregateService implements VersionAggregateService {
    public final SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    public SpyReturnValue<VersionAggregate> readVersionAggregateResults = new SpyReturnValue<>("readVersionAggregate");


    @Override
    public VersionAggregate readVersionAggregate(@NonNull VersionData version) throws RepoException {
        this.spyHelper.reportMethodCall("readVersionAggregate", version);
        this.spyHelper.checkThrowException();
        return this.readVersionAggregateResults.next();
    }
}
