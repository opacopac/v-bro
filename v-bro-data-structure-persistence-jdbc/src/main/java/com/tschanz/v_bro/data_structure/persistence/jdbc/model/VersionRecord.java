package com.tschanz.v_bro.data_structure.persistence.jdbc.model;

import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class VersionRecord {
    private final RepoTableRecord record;
}
