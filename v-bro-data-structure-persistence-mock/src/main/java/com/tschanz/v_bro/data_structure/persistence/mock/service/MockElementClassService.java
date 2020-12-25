package com.tschanz.v_bro.data_structure.persistence.mock.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.ElementClassService;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.List;


public class MockElementClassService implements ElementClassService {
    @Override
    public List<ElementClass> readAllElementClasses() throws RepoException {
        return List.of(
            new ElementClass("A_SORTIMENT_E"),
            new ElementClass("P_PRODUKTDEF_E")
        );
    }
}
