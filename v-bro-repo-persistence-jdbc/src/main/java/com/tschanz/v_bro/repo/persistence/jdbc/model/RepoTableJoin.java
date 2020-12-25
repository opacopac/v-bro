package com.tschanz.v_bro.repo.persistence.jdbc.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class RepoTableJoin {
    @NonNull private final RepoTable primaryTable;
    @NonNull private final RepoTable secondaryTable;
    @NonNull private final RepoField primaryTableField;
    @NonNull private final RepoField secondaryTableField;
}
