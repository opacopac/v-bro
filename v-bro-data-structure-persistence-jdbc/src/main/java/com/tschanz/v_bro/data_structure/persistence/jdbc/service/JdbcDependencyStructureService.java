package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.DependencyStructureService;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;


public class JdbcDependencyStructureService implements DependencyStructureService {
    @Override
    public List<ElementClass> readFwdDependencies(@NonNull ElementClass elementClass) {
        return Collections.emptyList(); // TODO
    }


    @Override
    public List<ElementClass> readBwdDependencies(@NonNull ElementClass elementClass) {
        return Collections.emptyList(); // TODO
    }
}
