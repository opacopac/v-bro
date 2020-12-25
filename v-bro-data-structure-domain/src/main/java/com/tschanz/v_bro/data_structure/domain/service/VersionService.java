package com.tschanz.v_bro.data_structure.domain.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;

import java.util.List;


public interface VersionService {
    List<VersionData> readVersions(@NonNull ElementData element) throws RepoException;
}
