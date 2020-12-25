package com.tschanz.v_bro.repo.persistence.jdbc.mock;

import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableJoin;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.JdbcQueryBuilder;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;
import lombok.NonNull;

import java.sql.SQLException;
import java.util.List;


public class SpyQueryBuilder implements JdbcQueryBuilder {
    public SpyHelper<SQLException> spyHelper = new SpyHelper<>();
    public String query;


    @Override
    public String buildQuery(
        @NonNull String tableName,
        @NonNull List<RepoTableJoin> tableJoins,
        @NonNull List<RepoField> selectFields,
        @NonNull List<RowFilter> mandatoryFilters,
        @NonNull List<RowFilter> optFilters,
        int maxResults
    ) {
        this.spyHelper.reportMethodCall("buildQuery", tableName, selectFields, mandatoryFilters, optFilters, maxResults);
        return this.query;
    }
}
