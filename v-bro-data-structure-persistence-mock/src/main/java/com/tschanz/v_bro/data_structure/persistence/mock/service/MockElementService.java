package com.tschanz.v_bro.data_structure.persistence.mock.service;

import com.tschanz.v_bro.data_structure.domain.model.DenominationData;
import com.tschanz.v_bro.data_structure.domain.service.ElementService;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.Collection;
import java.util.List;


public class MockElementService implements ElementService {
    @Override
    public List<ElementData> readElements(String elementClass, Collection<String> fieldNames, String query, int maxResults) throws RepoException {
        return List.of(
            new ElementData(
                "111",
                List.of(
                    new DenominationData("PRODUKTNUMMER", "125"),
                    new DenominationData("BEZEICHNUNG", "Gewöhnliche Billette Schweiz")
                )
            ),
            new ElementData(
                "222",
                List.of(
                    new DenominationData("PRODUKTNUMMER", "7"),
                    new DenominationData("BEZEICHNUNG", "Kintertageskarte")
                )
            )
        );
    }
}
