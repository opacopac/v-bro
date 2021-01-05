package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.DependencyStructureService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class JdbcDependencyStructureService implements DependencyStructureService {
    private final JdbcDataStructureService jdbcDataStructureService;


    @Override
    public List<ElementClass> readFwdDependencies(@NonNull ElementClass elementClass) {
        var aggregateStruct = this.jdbcDataStructureService.getAggregateStructureByElementClass(elementClass.getName());
        if (aggregateStruct != null) {
            return aggregateStruct.getFwdDepdendencyRelations()
                .stream()
                .map(rel -> new ElementClass(rel.getFwdClassName()))
                .distinct()
                .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }


    @Override
    public List<ElementClass> readBwdDependencies(@NonNull ElementClass elementClass) {
        var aggregateStruct = this.jdbcDataStructureService.getAggregateStructureByElementClass(elementClass.getName());
        if (aggregateStruct != null) {
            return aggregateStruct.getBwdDependencyRelations()
                .stream()
                .map(rel -> this.jdbcDataStructureService.getAggregateStructureContainingTable(rel.getBwdClassName()))
                .map(agg -> new ElementClass(agg.getElementTable().getName()))
                .distinct()
                .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
