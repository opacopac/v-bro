package com.tschanz.v_bro.element_classes.persistence.mock.service;

import com.tschanz.v_bro.common.testing.MockHelper;
import com.tschanz.v_bro.common.testing.MockReturnValue;
import com.tschanz.v_bro.element_classes.domain.model.Denomination;
import com.tschanz.v_bro.element_classes.domain.model.ElementClass;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.Collection;
import java.util.List;


public class MockElementClassService implements ElementClassService {
    public final MockHelper<RepoException> mockHelper = new MockHelper<>();
    public MockReturnValue<List<ElementClass>> readElementClassesResult = new MockReturnValue<>("readElementClasses");
    public MockReturnValue<List<Denomination>> readDenominationsResults = new MockReturnValue<>("readDenominations");


    @Override
    public List<ElementClass> readElementClasses() throws RepoException {
        this.mockHelper.reportMethodCall("readElementClasses");
        this.mockHelper.checkThrowException();
        return this.readElementClassesResult.next();
    }


    @Override
    public List<Denomination> readDenominations(String elementClass) throws RepoException {
        this.mockHelper.reportMethodCall("readDenominations");
        this.mockHelper.checkThrowException();
        return this.readDenominationsResults.next();
    }
}
