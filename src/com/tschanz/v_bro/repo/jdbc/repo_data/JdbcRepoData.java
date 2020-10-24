package com.tschanz.v_bro.repo.jdbc.repo_data;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.jdbc.repo_connection.JdbcConnectionFactory;
import com.tschanz.v_bro.repo.jdbc.model.FieldValue;
import com.tschanz.v_bro.repo.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.jdbc.model.RowInfo;
import com.tschanz.v_bro.repo.jdbc.querybuilder.JdbcQueryBuilder;
import com.tschanz.v_bro.repo.jdbc.querybuilder.RowFilter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class JdbcRepoData {
    private final Logger logger = Logger.getLogger(JdbcRepoData.class.getName());
    private final JdbcConnectionFactory connectionFactory;
    private final JdbcQueryBuilder queryBuilder;


    public JdbcRepoData(
        JdbcConnectionFactory connectionFactory,
        JdbcQueryBuilder queryBuilder
    ) {
        this.connectionFactory = connectionFactory;
        this.queryBuilder = queryBuilder;
    }


    public List<RowInfo> readData(String tableName, List<RepoField> fields, List<RowFilter> rowFilters) throws RepoException {
        this.logger.info("reading rows from table " + tableName);

        ArrayList<RowInfo> rows = new ArrayList<>();
        try {
            String query = this.queryBuilder.buildQuery(tableName, fields, rowFilters);
            Statement statement = this.connectionFactory.getCurrentConnection().createStatement();

            this.logger.info("executing query " + query);

            if (statement.execute(query)) {
                while (statement.getResultSet().next()) {
                    rows.add(this.parseRow(fields, statement.getResultSet()));
                }
            }
        } catch (SQLException exception) {
            String msg = "error reading rows: " + exception.getMessage();
            this.logger.severe(msg);
            throw new RepoException(msg, exception);
        }

        return rows;
    }


    private RowInfo parseRow(List<RepoField> fields, ResultSet resultSet) throws SQLException {
        Map<String, FieldValue> fieldValueMap = new HashMap<>();

        for (RepoField field: fields) {
            fieldValueMap.put(
                field.getName(),
                this.parseFieldValue(field, resultSet)
            );
        }

        return new RowInfo(fieldValueMap);
    }


    private FieldValue parseFieldValue(RepoField field, ResultSet resultSet) throws SQLException {
        switch (field.getType()) {
            case BOOL:
                return new FieldValue(field, resultSet.getBoolean(this.queryBuilder.createFieldName(field)));
            case LONG:
                return new FieldValue(field, resultSet.getLong(this.queryBuilder.createFieldName(field)));
            case DATE:
                return new FieldValue(field, resultSet.getDate(this.queryBuilder.createFieldName(field)));
            case STRING:
            default:
                return new FieldValue(field, resultSet.getString(this.queryBuilder.createFieldName(field)));
        }
    }
}
