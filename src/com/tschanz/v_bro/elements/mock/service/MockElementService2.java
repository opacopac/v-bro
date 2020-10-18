package com.tschanz.v_bro.elements.mock.service;

import com.tschanz.v_bro.elements.domain.model.ElementClass;
import com.tschanz.v_bro.elements.domain.model.NameFieldData;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.elements.domain.model.NameField;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.Collection;
import java.util.List;


public class MockElementService2 implements ElementService {
    @Override
    public Collection<ElementClass> readElementClasses() throws RepoException {
        return List.of(
            new ElementClass("A_SORTIMENT_E"),
            new ElementClass("P_PRODUKTDEF_E")
        );
    }


    @Override
    public List<NameField> readNameFields(String elementName) throws RepoException {
        return List.of(
            new NameField("CODE"),
            new NameField("BEZEICHNUNG")
        );
    }


    @Override
    public Collection<ElementData> readElementData(String elementName, Collection<String> fieldNames) throws RepoException {
        return List.of(
            new ElementData(
                "111",
                List.of(
                    new NameFieldData("PRODUKTNUMMER", "125"),
                    new NameFieldData("BEZEICHNUNG", "Gewöhnliche Billette Schweiz")
                )
            ),
            new ElementData(
                "222",
                List.of(
                    new NameFieldData("PRODUKTNUMMER", "7"),
                    new NameFieldData("BEZEICHNUNG", "Kintertageskarte")
                )
            )
        );
    }
}
