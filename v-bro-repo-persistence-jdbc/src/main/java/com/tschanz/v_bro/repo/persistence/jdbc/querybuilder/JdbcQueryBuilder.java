package com.tschanz.v_bro.repo.persistence.jdbc.querybuilder;

import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import lombok.NonNull;

import java.util.List;


public interface JdbcQueryBuilder {
    String buildQuery(
        @NonNull String tableName,
        @NonNull List<RepoField> selectFields,
        @NonNull List<RowFilter> andFilters,
        @NonNull List<RowFilter> orFilters,
        int maxResults
    );
}
