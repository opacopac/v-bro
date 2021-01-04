package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import lombok.NonNull;

import java.util.List;


public interface ElementService {
    List<ElementData> queryElements(
        @NonNull ElementClass elementClass,
        @NonNull List<Denomination> denominationFields,
        @NonNull String query,
        int maxResults
    );


    ElementData readElement(
        @NonNull ElementClass elementClass,
        @NonNull List<Denomination> denominationFields,
        @NonNull String elementId
    );
}
