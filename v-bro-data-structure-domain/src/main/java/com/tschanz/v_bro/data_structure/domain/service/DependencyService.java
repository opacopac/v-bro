package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.data_structure.domain.model.Dependency;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;

import java.util.List;


public interface DependencyService {
    List<Dependency> readFwdDependencies(@NonNull VersionData version) throws RepoException;

    List<Dependency> readBwdDependencies(@NonNull ElementData element) throws RepoException;
}
