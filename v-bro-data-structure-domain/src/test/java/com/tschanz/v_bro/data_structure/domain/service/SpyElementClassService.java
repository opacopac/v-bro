package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.common.testing.SpyReturnValue;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.List;


public class SpyElementClassService implements ElementClassService {
    public final SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    public SpyReturnValue<List<ElementClass>> readElementClassesResult = new SpyReturnValue<>("readElementClasses");


    @Override
    public List<ElementClass> readAllElementClasses() throws RepoException {
        this.spyHelper.reportMethodCall("readElementClasses");
        this.spyHelper.checkThrowException();
        return this.readElementClassesResult.next();
    }
}
