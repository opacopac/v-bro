package com.tschanz.v_bro.repo.persistence.jdbc.querybuilder;

import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableJoin;
import lombok.NonNull;

import java.util.List;


public interface JdbcQueryBuilder {
    String buildQuery(
        @NonNull String tableName,
        @NonNull List<RepoTableJoin> tableJoins,
        @NonNull List<RepoField> selectFields,
        @NonNull List<RowFilter> mandatoryFilters,
        @NonNull List<RowFilter> optFilters,
        int maxResults
    );
}
