package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.data_structure.domain.model.FwdDependency;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;

import java.util.List;


public interface DependencyService {
    List<FwdDependency> readFwdDependencies(@NonNull VersionData version) throws RepoException;
}
