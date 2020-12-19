package com.tschanz.v_bro.repo.persistence.jdbc.repo_data;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.*;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.JdbcQueryBuilder;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Log
@RequiredArgsConstructor
public class JdbcRepoDataService {
    private final JdbcConnectionFactory connectionFactory;
    private final JdbcQueryBuilder queryBuilder;


    public List<RepoTableRecord> readRepoTableRecords(RepoTable repoTable, List<RepoField> fields, List<RowFilter> andFilters, List<RowFilter> orFilters, int maxResults) throws RepoException {
        log.info("reading rows from table " + repoTable.getName());

        ArrayList<RowInfo> rows = new ArrayList<>();
        try {
            var query = this.queryBuilder.buildQuery(repoTable.getName(), fields, andFilters, orFilters, maxResults);
            var statement = this.connectionFactory.getCurrentConnection().createStatement();

            log.info("executing query " + query);
            AtomicReference<SQLException> readException = new AtomicReference<>();
            // reason for ugly hack with separate thread: caller (e.g. controlfx autocomplete) may interrupt the thread, resulting in a db connection close in some cases
            Thread t = new Thread(() -> {
                try {
                    if (statement.execute(query)) {
                        while (statement.getResultSet().next()) {
                            rows.add(this.parseRow(fields, statement.getResultSet()));
                        }
                        statement.getResultSet().close();
                    }
                } catch (SQLException exception) {
                    readException.set(exception);
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException exception) {
                statement.cancel();
            }
            statement.close();

            if (readException.get() != null) {
                String msg = "error reading rows: " + readException.get().getMessage();
                log.severe(msg);
                throw new RepoException(msg, readException.get());
            }
        } catch (SQLException exception) {
            String msg = "error reading rows: " + exception.getMessage();
            log.severe(msg);
            throw new RepoException(msg, exception);
        }

        return rows
            .stream()
            .map(row -> new RepoTableRecord(repoTable, new ArrayList<>(row.getAllFieldValues())))
            .collect(Collectors.toList());
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
                var boolResult = resultSet.getBoolean(field.getName());
                return new FieldValue(field, resultSet.wasNull() ? null : boolResult);
            case LONG:
                var longResult = resultSet.getLong(field.getName());
                return new FieldValue(field, resultSet.wasNull() ? null : longResult);
            case DATE:
                var dateResult = resultSet.getDate(field.getName());
                return new FieldValue(field, resultSet.wasNull() ? null : dateResult);
            case TIMESTAMP:
                var timestampResult = resultSet.getTimestamp(field.getName());
                return new FieldValue(field, resultSet.wasNull() ? null : timestampResult);
            case STRING:
            default:
                var stringResult = resultSet.getString(field.getName());
                return new FieldValue(field, resultSet.wasNull() ? null : stringResult);
        }
    }
}
