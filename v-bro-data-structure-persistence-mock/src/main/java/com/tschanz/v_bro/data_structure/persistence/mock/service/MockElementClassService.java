package com.tschanz.v_bro.data_structure.persistence.mock.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.ElementClassService;

import java.util.List;


public class MockElementClassService implements ElementClassService {
    @Override
    public List<ElementClass> readAllElementClasses() {
        return List.of(
            new ElementClass("A_SORTIMENT_E"),
            new ElementClass("P_PRODUKTDEF_E")
        );
    }
}
