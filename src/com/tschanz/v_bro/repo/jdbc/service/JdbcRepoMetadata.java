package com.tschanz.v_bro.repo.jdbc.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.jdbc.model.*;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class JdbcRepoMetadata implements RepoMetadata {
    public static String WILDCARD = "%";

    private final Logger logger = Logger.getLogger(JdbcRepoMetadata.class.getName());
    private final JdbcConnectionFactory connectionFactory;


    public JdbcRepoMetadata(JdbcConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    // region readTableStructure

    @Override
    public RepoTable readTableStructure(String tableName) throws RepoException {
        tableName = this.readTable(tableName);
        List<String> pks = this.readPrimaryKeys(tableName);
        List<String> uniqueIdxes = this.readIndexes(tableName, true);
        List<RepoField> repoFields = this.readFields(tableName, pks, uniqueIdxes);
        List<RepoRelation> outgoingRelations = this.readRelations(tableName, false);
        List<RepoRelation> incomingRelations = this.readRelations(tableName, true);

        return new RepoTable(
            tableName,
            repoFields,
            outgoingRelations,
            incomingRelations
        );
    }

    // endregion


    // region readTable

    @Override
    public String readTable(String tableName) throws RepoException {
        if (tableName == null || tableName.isEmpty()) {
            throw new RepoException("tableName must not be null or empty", null);
        }

        this.logger.info("reading table " + tableName);


        String table = null;
        try {
            ResultSet tablesResult = this.readTableResults(this.escapeUnderscore(tableName));
            if (tablesResult.next()) {
                table = tablesResult.getString("TABLE_NAME");
            }
            tablesResult.close();
        } catch (SQLException exception) {
            String msg = "error finding table: " + exception.getMessage();
            this.logger.severe (msg);
            throw new RepoException(msg, exception);
        }

        if (table == null) {
            String msg = "table " + tableName + " not found.";
            this.logger.severe (msg);
            throw new RepoException(msg, null);
        }

        return table;
    }

    // endregion


    // region readPrimaryKeys

    @Override
    public List<String> readPrimaryKeys(String tableName) throws RepoException {
        this.logger.info("reading primary keys for table " + tableName);

        List<String> primaryKeys = new ArrayList<>();
        try {
            ResultSet pkResult = this.connectionFactory.getCurrentConnection().getMetaData().getPrimaryKeys(null,null, tableName);

            while (pkResult.next()) {
                primaryKeys.add(pkResult.getString("COLUMN_NAME"));
            }
            pkResult.close();
        } catch (SQLException exception) {
            String msg = "error primary keys: " + exception.getMessage();
            this.logger.severe(msg);
            throw new RepoException(msg, exception);
        }

        return primaryKeys;
    }

    // endregion


    // region readIndexes

    @Override
    public List<String> readIndexes(String tableName, boolean uniqueOnly) throws RepoException {
        this.logger.info("reading indexes for table " + tableName);

        ArrayList<String> indexInfos = new ArrayList<>();
        try {
            ResultSet indexResults = this.connectionFactory
                .getCurrentConnection()
                .getMetaData()
                .getIndexInfo(null, null, tableName, uniqueOnly, false);

            while (indexResults.next()) {
                if (indexResults.getString("INDEX_NAME") == null) {
                    continue;
                }
                indexInfos.add(indexResults.getString("COLUMN_NAME"));
            }
            indexResults.close();
        } catch (SQLException exception) {
            String msg = "error reading indexes: " + exception.getMessage();
            this.logger.severe(msg);
            throw new RepoException(msg, exception);
        }

        return indexInfos;
    }

    // endregion


    // region readFields

    @Override
    public List<RepoField> readFields(String tableName, List<String> pks, List<String> uniqueIdxes) throws RepoException {
        this.logger.info("reading columns for table " + tableName);

        List<RepoField> repoFields = new ArrayList<>();
        try {
            ResultSet columnsResult = this.connectionFactory.getCurrentConnection().getMetaData().getColumns(null,null, tableName, null);
            while (columnsResult.next()) {
                String fieldName = columnsResult.getString("COLUMN_NAME");
                RepoFieldType fieldType = this.getFieldType(columnsResult.getInt("DATA_TYPE"));
                boolean isNullable = this.isNullable(columnsResult.getInt("NULLABLE"));
                boolean isId = pks.contains(fieldName);
                boolean isUnique = uniqueIdxes.contains(fieldName);
                repoFields.add(new RepoField(fieldName, fieldType, isId, isNullable, isUnique));
            }
            columnsResult.close();
        } catch (SQLException exception) {
            String msg = "error reading columns: " + exception.getMessage();
            this.logger.severe(msg);
            throw new RepoException(msg, exception);
        }

        return repoFields;
    }


    private RepoFieldType getFieldType(int dataType) {
        switch (dataType) {
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.SMALLINT:
            case Types.TINYINT:
                return RepoFieldType.LONG;
            case Types.BOOLEAN:
                return RepoFieldType.BOOL;
            case Types.DATE:
                return RepoFieldType.DATE;
            case Types.VARCHAR:
            case Types.NVARCHAR:
            default:
                return RepoFieldType.STRING;
        }
    }


    private boolean isNullable(int nullableType) {
        switch (nullableType) {
            case DatabaseMetaData.columnNoNulls:
                return false;
            case DatabaseMetaData.columnNullable:
            default:
                return true;
        }
    }

    // endregion


    // region readRelations

    @Override
    public List<RepoRelation> readRelations(String tableName, boolean incoming) throws RepoException {
        this.logger.info("reading relations for table " + tableName);

        List<RepoRelation> repoRelations = new ArrayList<>();
        try {
            ResultSet relationsResult = incoming ?
                this.connectionFactory.getCurrentConnection().getMetaData().getExportedKeys(null,null, tableName) :
                this.connectionFactory.getCurrentConnection().getMetaData().getImportedKeys(null, null, tableName);

            while (relationsResult.next()) {
                RepoRelation repoRelation = new RepoRelation(
                    relationsResult.getString("FKTABLE_NAME"),
                    relationsResult.getString("FKCOLUMN_NAME"),
                    relationsResult.getString("PKTABLE_NAME"),
                    relationsResult.getString("PKCOLUMN_NAME")
                );
                repoRelations.add(repoRelation);
            }
            relationsResult.close();
        } catch (SQLException exception) {
            String msg = "error reading relations: " + exception.getMessage();
            this.logger.severe(msg);
            throw new RepoException(msg, exception);
        }

        return repoRelations;
    }

    // endregion


    @Override
    public String escapeUnderscore(String tableNamePattern) throws SQLException {
        String escapeChar = this.connectionFactory.getCurrentConnection().getMetaData().getSearchStringEscape();
        return tableNamePattern.replace("_", escapeChar + "_");
    }


    @Override
    public ResultSet readTableResults(String tableNamePattern) throws SQLException {
        return this.connectionFactory.getCurrentConnection().getMetaData().getTables(
            null,
            null,
            tableNamePattern,
            new String[]{"TABLE"}
        );
    }
}
