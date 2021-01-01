package com.tschanz.v_bro.repo.persistence.jdbc.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class RepoRelation {
    @NonNull private final String bwdClassName;
    @NonNull private final String bwdFieldName;
    @NonNull private final String fwdClassName;
    @NonNull private final String fwdFieldName;
}
