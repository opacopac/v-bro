package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.data_structure.domain.model.FwdDependency;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.List;


public interface DependencyService {
    List<FwdDependency> readFwdDependencies(String elemenClass, String elementId, String versionId) throws RepoException;
}
