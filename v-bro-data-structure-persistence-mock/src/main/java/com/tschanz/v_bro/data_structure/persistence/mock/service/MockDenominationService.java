package com.tschanz.v_bro.data_structure.persistence.mock.service;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.DenominationService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;

import java.util.List;


public class MockDenominationService implements DenominationService {
    @Override
    public List<Denomination> readDenominations(@NonNull ElementClass elementClass) throws RepoException {
        return List.of(
            new Denomination("P_PRODUKTDEF_E", "CODE"),
            new Denomination("P_PRODUKTDEF_E", "BEZEICHNUNG")
        );
    }
}
