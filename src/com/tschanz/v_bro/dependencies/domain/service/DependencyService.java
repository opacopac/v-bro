package com.tschanz.v_bro.dependencies.domain.service;

import com.tschanz.v_bro.dependencies.domain.model.FwdDependency;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.Collection;


public interface DependencyService {
    Collection<FwdDependency> readFwdDependencies(String elemenClass, String elementId, String versionId) throws RepoException;
}