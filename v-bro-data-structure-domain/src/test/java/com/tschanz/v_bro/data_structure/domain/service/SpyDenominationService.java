package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.common.testing.SpyReturnValue;
import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;

import java.util.List;


public class SpyDenominationService implements DenominationService {
    public final SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    public SpyReturnValue<List<Denomination>> readDenominationsResults = new SpyReturnValue<>("readDenominations");


    @Override
    public List<Denomination> readDenominations(@NonNull ElementClass elementClass) throws RepoException {
        this.spyHelper.reportMethodCall("readDenominations", elementClass);
        this.spyHelper.checkThrowException();
        return this.readDenominationsResults.next();
    }
}
