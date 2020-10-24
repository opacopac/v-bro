package com.tschanz.v_bro.versioning.domain.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.versioning.domain.model.VersionAggregate;
import com.tschanz.v_bro.versioning.domain.model.VersionInfo;

import java.util.Collection;


public interface VersionService {
    Collection<VersionInfo> readVersionTimeline(String elementName, String elementId) throws RepoException;

    VersionAggregate readVersionAggregate(String elementName, String elementId, String versionId) throws RepoException;
}
