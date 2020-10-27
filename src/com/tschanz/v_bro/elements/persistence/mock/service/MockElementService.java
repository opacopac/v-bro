package com.tschanz.v_bro.elements.persistence.mock.service;

import com.tschanz.v_bro.common.testing.MockHelper;
import com.tschanz.v_bro.common.testing.MockReturnValue;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.ArrayList;
import java.util.Collection;


public class MockElementService implements ElementService {
    public final MockHelper<RepoException> mockHelper = new MockHelper<>();
    public MockReturnValue<Collection<ElementData>> readElementDataResults = new MockReturnValue<>("readElementData");


    @Override
    public Collection<ElementData> readElements(String elementName, Collection<String> fieldNames) throws RepoException {
        this.mockHelper.reportMethodCall("readElementData", elementName, fieldNames);
        this.mockHelper.checkThrowException();
        return new ArrayList<>(this.readElementDataResults.next());
    }
}
