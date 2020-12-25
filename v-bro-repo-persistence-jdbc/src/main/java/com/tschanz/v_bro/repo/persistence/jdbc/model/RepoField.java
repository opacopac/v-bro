package com.tschanz.v_bro.repo.persistence.jdbc.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class RepoField {
    @NonNull private final String tableName;
    @NonNull private final String name;
    @NonNull private final RepoFieldType type;
    @NonNull private final boolean isId;
    @NonNull private final boolean isNullable;
    @NonNull private final boolean isUnique;
}
