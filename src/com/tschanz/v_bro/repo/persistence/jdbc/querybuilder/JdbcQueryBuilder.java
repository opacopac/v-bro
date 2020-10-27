package com.tschanz.v_bro.repo.persistence.jdbc.querybuilder;

import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;

import java.util.List;


public interface JdbcQueryBuilder {
    String buildQuery(String tableName, List<RepoField> selectFields, List<RowFilter> rowFilters);

    String createFieldName(RepoField field);
}
