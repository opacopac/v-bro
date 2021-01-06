package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.ElementClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.List;
import java.util.stream.Collectors;


@Log
@RequiredArgsConstructor
public class JdbcElementClassService implements ElementClassService {
    private final JdbcDataStructureService dataStructureService;


    @Override
    public List<ElementClass> readAllElementClasses() {
        log.info("finding all element classes");

        return this.dataStructureService.getAggregateStructures()
            .stream()
            .map(agg -> agg.getElementTable().getName())
            .sorted()
            .map(ElementClass::new)
            .collect(Collectors.toList());
    }
}
