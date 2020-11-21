package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.List;


public interface VersionService {
    List<VersionData> readVersionTimeline(String elementClass, String elementId) throws RepoException;
}
