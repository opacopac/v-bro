package com.tschanz.v_bro.repo.persistence.jdbc.mock;

import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.JdbcQueryBuilder;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;

import java.sql.SQLException;
import java.util.List;


public class SpyQueryBuilder implements JdbcQueryBuilder {
    public SpyHelper<SQLException> spyHelper = new SpyHelper<>();
    public String query;


    @Override
    public String buildQuery(String tableName, List<RepoField> selectFields, List<RowFilter> rowFilters) {
        this.spyHelper.reportMethodCall("buildQuery", tableName, selectFields, rowFilters);
        return this.query;
    }
}
