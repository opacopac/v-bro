package com.tschanz.v_bro.structure.persistence.mock.service;

import com.tschanz.v_bro.structure.domain.model.Denomination;
import com.tschanz.v_bro.structure.domain.model.ElementClass;
import com.tschanz.v_bro.structure.domain.service.ElementClassService;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.List;


public class MockElementClassService implements ElementClassService {
    @Override
    public List<ElementClass> readElementClasses() throws RepoException {
        return List.of(
            new ElementClass("A_SORTIMENT_E"),
            new ElementClass("P_PRODUKTDEF_E")
        );
    }


    @Override
    public List<Denomination> readDenominations(String elementClass) throws RepoException {
        return List.of(
            new Denomination("CODE"),
            new Denomination("BEZEICHNUNG")
        );
    }
}
