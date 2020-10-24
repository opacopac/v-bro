package com.tschanz.v_bro.elements.mock.service;

import com.tschanz.v_bro.common.test.MockHelper;
import com.tschanz.v_bro.common.test.MockReturnValue;
import com.tschanz.v_bro.elements.domain.model.ElementClass;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.elements.domain.model.Denomination;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MockElementService implements ElementService {
    public final MockHelper<RepoException> mockHelper = new MockHelper<>();
    public MockReturnValue<List<ElementClass>> readElementClassesResult = new MockReturnValue<>("readElementClasses");
    public MockReturnValue<List<Denomination>> readNameFieldsResults = new MockReturnValue<>("readNameFields");
    public MockReturnValue<Collection<ElementData>> readElementDataResults = new MockReturnValue<>("readElementData");


    @Override
    public Collection<ElementClass> readElementClasses() throws RepoException {
        this.mockHelper.reportMethodCall("findElementTableNames");
        this.mockHelper.checkThrowException();
        return this.readElementClassesResult.next();
    }


    @Override
    public List<Denomination> readDenominations(String elementName) throws RepoException {
        this.mockHelper.reportMethodCall("readNameFields");
        this.mockHelper.checkThrowException();
        return this.readNameFieldsResults.next();
    }


    @Override
    public Collection<ElementData> readElements(String elementName, Collection<String> fieldNames) throws RepoException {
        this.mockHelper.reportMethodCall("readElementData", elementName, fieldNames);
        this.mockHelper.checkThrowException();
        return new ArrayList<>(this.readElementDataResults.next());
    }
}
