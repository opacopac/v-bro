package com.tschanz.v_bro.repo.jdbc.mock;

import com.tschanz.v_bro.repo.jdbc.model.RepoField;
import com.tschanz.v_bro.common.testing.MockHelper;
import com.tschanz.v_bro.repo.jdbc.querybuilder.JdbcQueryBuilder;
import com.tschanz.v_bro.repo.jdbc.querybuilder.RowFilter;

import java.sql.SQLException;
import java.util.List;


public class MockQueryBuilder implements JdbcQueryBuilder {
    public MockHelper<SQLException> mockHelper = new MockHelper<>();
    public String query;


    @Override
    public String buildQuery(String tableName, List<RepoField> selectFields, List<RowFilter> rowFilters) {
        this.mockHelper.reportMethodCall("buildQuery", tableName, selectFields, rowFilters);
        return this.query;
    }


    @Override
    public String createFieldName(RepoField field) {
        return null; // TODO
    }
}
