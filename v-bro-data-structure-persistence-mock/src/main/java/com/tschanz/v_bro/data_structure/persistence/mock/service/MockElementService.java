package com.tschanz.v_bro.data_structure.persistence.mock.service;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.DenominationData;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.service.ElementService;
import lombok.NonNull;

import java.util.List;


public class MockElementService implements ElementService {
    @Override
    public List<ElementData> queryElements(
        @NonNull ElementClass elementClass,
        @NonNull List<Denomination> denominationFields,
        @NonNull String query,
        int maxResults
    ) {
        return List.of(
            new ElementData(
                elementClass,
                "111",
                List.of(
                    new DenominationData("PRODUKTNUMMER", "125"),
                    new DenominationData("BEZEICHNUNG", "Gewöhnliche Billette Schweiz")
                )
            ),
            new ElementData(
                elementClass,
                "222",
                List.of(
                    new DenominationData("PRODUKTNUMMER", "7"),
                    new DenominationData("BEZEICHNUNG", "Kintertageskarte")
                )
            )
        );
    }


    @Override
    public ElementData readElement(
        @NonNull ElementClass elementClass,
        @NonNull List<Denomination> denominationFields,
        @NonNull String elementId
    ) {
        return new ElementData(
            elementClass,
            "111",
            List.of(
                new DenominationData("PRODUKTNUMMER", "125"),
                new DenominationData("BEZEICHNUNG", "Gewöhnliche Billette Schweiz")
            )
        );
    }
}
