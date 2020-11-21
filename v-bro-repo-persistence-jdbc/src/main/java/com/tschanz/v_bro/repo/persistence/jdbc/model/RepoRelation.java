package com.tschanz.v_bro.repo.persistence.jdbc.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RepoRelation {
    @Getter private final String bwdClassName;
    @Getter private final String bwdFieldName;
    @Getter private final String fwdClassName;
    @Getter private final String fwdFieldName;
}
