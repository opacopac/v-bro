package com.tschanz.v_bro.repo.persistence.jdbc.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class RepoField {
    @NonNull private final String tableName;
    @NonNull private final String name;
    @NonNull private final RepoFieldType type;
    @NonNull private final boolean isPk;
    @NonNull private final boolean isNullable;
    @NonNull private final boolean isUnique;


    public RepoField copyWithNewType(RepoFieldType newType) {
        return new RepoField(
            this.tableName,
            this.name,
            newType,
            this.isPk,
            this.isNullable,
            this.isUnique
        );
    }
}
