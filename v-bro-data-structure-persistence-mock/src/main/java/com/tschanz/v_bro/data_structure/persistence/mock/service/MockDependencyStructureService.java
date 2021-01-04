package com.tschanz.v_bro.data_structure.persistence.mock.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.DependencyStructureService;
import lombok.NonNull;

import java.util.List;


public class MockDependencyStructureService implements DependencyStructureService {
    @Override
    public List<ElementClass> readFwdDependencies(@NonNull ElementClass elementClass) {
        return List.of(
            new ElementClass("P_PRODUKTDEF_E"),
            new ElementClass("P_SORTIMENT_E")
        );
    }


    @Override
    public List<ElementClass> readBwdDependencies(@NonNull ElementClass elementClass) {
        return List.of(
            new ElementClass("P_SORTIMENT_E")
        );
    }
}
