package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.common.testing.SpyReturnValue;
import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;


public class SpyElementService implements ElementService {
    public final SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    public SpyReturnValue<List<ElementData>> queryElementsResults = new SpyReturnValue<>("queryElements");
    public SpyReturnValue<ElementData> readElementResults = new SpyReturnValue<>("readElement");


    @Override
    public List<ElementData> queryElements(
        @NonNull ElementClass elementClass,
        @NonNull List<Denomination> denominationFields,
        @NonNull String query,
        int maxResults
    ) throws RepoException {
        this.spyHelper.reportMethodCall("queryElements", elementClass, denominationFields, query, maxResults);
        this.spyHelper.checkThrowException();
        return new ArrayList<>(this.queryElementsResults.next());
    }


    @Override
    public ElementData readElement(
        @NonNull ElementClass elementClass,
        @NonNull List<Denomination> denominationFields,
        @NonNull String elementId
    ) throws RepoException {
        this.spyHelper.reportMethodCall("readElement", elementClass, denominationFields, elementId);
        this.spyHelper.checkThrowException();
        return this.readElementResults.next();
    }
}
