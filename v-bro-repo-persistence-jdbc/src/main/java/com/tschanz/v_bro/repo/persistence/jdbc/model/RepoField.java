package com.tschanz.v_bro.repo.persistence.jdbc.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class RepoField {
    public String tableName; // TODO
    @Getter private final String name;
    @Getter private final RepoFieldType type;
    @Getter private final boolean isId;
    @Getter private final boolean isNullable;
    @Getter private final boolean isUnique;
}
