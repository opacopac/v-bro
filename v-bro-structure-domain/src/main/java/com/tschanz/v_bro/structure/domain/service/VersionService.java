package com.tschanz.v_bro.structure.domain.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.structure.domain.model.VersionData;

import java.util.List;


public interface VersionService {
    List<VersionData> readVersionTimeline(String elementClass, String elementId) throws RepoException;
}
