package com.tschanz.v_bro.element_classes.persistence.mock;

import com.tschanz.v_bro.element_classes.domain.model.Denomination;
import com.tschanz.v_bro.element_classes.domain.model.ElementClass;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.Collection;
import java.util.List;


public class MockElementClassService2 implements ElementClassService {
    @Override
    public Collection<ElementClass> readElementClasses() throws RepoException {
        return List.of(
            new ElementClass("A_SORTIMENT_E"),
            new ElementClass("P_PRODUKTDEF_E")
        );
    }


    @Override
    public List<Denomination> readDenominations(String elementName) throws RepoException {
        return List.of(
            new Denomination("CODE"),
            new Denomination("BEZEICHNUNG")
        );
    }
}
