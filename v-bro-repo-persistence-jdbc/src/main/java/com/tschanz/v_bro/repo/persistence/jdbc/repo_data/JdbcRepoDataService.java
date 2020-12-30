package com.tschanz.v_bro.repo.persistence.jdbc.repo_data;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableJoin;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.JdbcQueryBuilder;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.RowFilter;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Log
@RequiredArgsConstructor
public class JdbcRepoDataService {
    private final JdbcConnectionFactory connectionFactory;
    private final JdbcQueryBuilder queryBuilder;


    public List<RepoTableRecord> readRepoTableRecords(
        RepoTable repoTable,
        List<RepoTableJoin> joins,
        List<RepoField> fields,
        List<RowFilter> mandatoryFilters,
        List<RowFilter> optFilters,
        int maxResults
    ) throws RepoException {
        ArrayList<RepoTableRecord> records = new ArrayList<>();
        try {
            var query = this.queryBuilder.buildQuery(repoTable.getName(), joins, fields, mandatoryFilters, optFilters, maxResults);
            var statement = this.connectionFactory.getCurrentConnection().createStatement();

            log.info("executing query " + query);
            AtomicReference<SQLException> readException = new AtomicReference<>();
            // reason for ugly hack with separate thread: caller (e.g. controlfx autocomplete) may interrupt the thread, resulting in a db connection close in some cases
            var t = new Thread(() -> {
                try {
                    if (statement.execute(query)) {
                        while (statement.getResultSet().next()) {
                            records.add(RepoTableRecord.fromResultSet(repoTable, fields, statement.getResultSet()));
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
                var msg = "error reading rows: " + readException.get().getMessage();
                log.severe(msg);
                throw new RepoException(msg, readException.get());
            }
        } catch (SQLException exception) {
            var msg = "error reading rows: " + exception.getMessage();
            log.severe(msg);
            throw new RepoException(msg, exception);
        }

        return records;
    }
}
