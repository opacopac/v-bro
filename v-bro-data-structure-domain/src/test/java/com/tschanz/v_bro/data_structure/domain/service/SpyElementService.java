package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.common.testing.SpyReturnValue;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SpyElementService implements ElementService {
    public final SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    public SpyReturnValue<List<ElementData>> readElementDataResults = new SpyReturnValue<>("readElementData");


    @Override
    public List<ElementData> readElements(String elementClass, Collection<String> fieldNames, String query, int maxResults) throws RepoException {
        this.spyHelper.reportMethodCall("readElementData", elementClass, fieldNames, query, maxResults);
        this.spyHelper.checkThrowException();
        return new ArrayList<>(this.readElementDataResults.next());
    }
}
