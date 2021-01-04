package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import lombok.NonNull;

import java.util.List;


public interface DenominationService {
    List<Denomination> readDenominations(@NonNull ElementClass elementClass);
}
