package com.tschanz.v_bro.versioning.domain.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.versioning.domain.model.VersionData;

import java.util.Collection;


public interface VersionService {
    Collection<VersionData> readVersions(String elementName, String elementId) throws RepoException;
}
