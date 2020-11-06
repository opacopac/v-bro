package com.tschanz.v_bro.element_classes.persistence.spy.service;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.common.testing.SpyReturnValue;
import com.tschanz.v_bro.element_classes.domain.model.Denomination;
import com.tschanz.v_bro.element_classes.domain.model.ElementClass;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.List;


public class SpyElementClassService implements ElementClassService {
    public final SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    public SpyReturnValue<List<ElementClass>> readElementClassesResult = new SpyReturnValue<>("readElementClasses");
    public SpyReturnValue<List<Denomination>> readDenominationsResults = new SpyReturnValue<>("readDenominations");


    @Override
    public List<ElementClass> readElementClasses() throws RepoException {
        this.spyHelper.reportMethodCall("readElementClasses");
        this.spyHelper.checkThrowException();
        return this.readElementClassesResult.next();
    }


    @Override
    public List<Denomination> readDenominations(String elementClass) throws RepoException {
        this.spyHelper.reportMethodCall("readDenominations");
        this.spyHelper.checkThrowException();
        return this.readDenominationsResults.next();
    }
}
