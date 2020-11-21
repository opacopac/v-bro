package com.tschanz.v_bro.versions.domain.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.versions.domain.model.VersionData;

import java.util.List;


public interface VersionService {
    List<VersionData> readVersionTimeline(String elementClass, String elementId) throws RepoException;
}
