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
    public SpyReturnValue<List<ElementData>> readElementsResults = new SpyReturnValue<>("readElements");


    @Override
    public List<ElementData> readElements(
        @NonNull ElementClass elementClass,
        @NonNull List<Denomination> denominationFields,
        @NonNull String query,
        int maxResults
    ) throws RepoException {
        this.spyHelper.reportMethodCall("readElements", elementClass, denominationFields, query, maxResults);
        this.spyHelper.checkThrowException();
        return new ArrayList<>(this.readElementsResults.next());
    }
}
