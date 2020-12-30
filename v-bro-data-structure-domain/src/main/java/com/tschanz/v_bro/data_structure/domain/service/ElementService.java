package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;

import java.util.List;


public interface ElementService {
    List<ElementData> queryElements(
        @NonNull ElementClass elementClass,
        @NonNull List<Denomination> denominationFields,
        @NonNull String query,
        int maxResults
    ) throws RepoException;


    ElementData readElement(
        @NonNull ElementClass elementClass,
        @NonNull List<Denomination> denominationFields,
        @NonNull String elementId
    ) throws RepoException;
}
