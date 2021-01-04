package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import lombok.NonNull;

import java.util.List;


public interface DependencyStructureService {
    List<ElementClass> readFwdDependencies(@NonNull ElementClass elementClass);

    List<ElementClass> readBwdDependencies(@NonNull ElementClass elementClass);
}
