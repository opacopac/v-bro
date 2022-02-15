package com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoFieldType;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Log
@RequiredArgsConstructor
public class JdbcRepoMetadataServiceImpl implements JdbcRepoMetadataService {
    public static final String WILDCARD = "%";
    private final JdbcConnectionFactory connectionFactory;
    private List<RepoField> repoFieldLut;
    private List<RepoRelation> repoRelationLut;


    @Override
    public RepoTable readTableStructure(String tableName) throws RepoException {
        var upperCaseTableName = tableName.toUpperCase();
        var repoFields = this.getRepoFieldLut()
            .stream()
            .filter(field -> field.getTableName().equals(upperCaseTableName))
            .collect(Collectors.toList());
        var outgoingRelations = this.getRepoRelationLut()
            .stream()
            .filter(relation -> relation.getBwdClassName().equals(upperCaseTableName))
            .collect(Collectors.toList());
        var incomingRelations = this.getRepoRelationLut()
            .stream()
            .filter(relation -> relation.getFwdClassName().equals(upperCaseTableName))
            .collect(Collectors.toList());

        return new RepoTable(
            upperCaseTableName,
            repoFields,
            outgoingRelations,
            incomingRelations
        );
    }


    public List<RepoField> getRepoFieldLut() throws RepoException {
        if (this.repoFieldLut == null) {
            this.repoFieldLut = this.readAllRepoFields();
        }

        return this.repoFieldLut;
    }


    public List<RepoRelation> getRepoRelationLut() throws RepoException {
        if (this.repoRelationLut == null) {
            this.repoRelationLut = this.readAllRelations();
        }

        return this.repoRelationLut;
    }


    private List<RepoRelation> readAllRelations() throws RepoException {
        String query;
        switch (this.connectionFactory.getJdbcServerType()) {
            case MYSQL:
                query = "select TABLE_NAME as BWD_TABLE, COLUMN_NAME as BWD_COLUMN, REFERENCED_TABLE_NAME as FWD_TABLE, REFERENCED_COLUMN_NAME as FWD_COLUMN "
                    + "from information_schema.key_column_usage "
                    + "where REFERENCED_TABLE_NAME IS NOT NULL AND constraint_schema = DATABASE()";
                break;
            case ORACLE:
            default:
                var schema = this.connectionFactory.getCurrentSchema();
                query = "SELECT con.TABLE_NAME AS BWD_TABLE, col.COLUMN_NAME AS BWD_COLUMN, col2.TABLE_NAME AS FWD_TABLE, col2.COLUMN_NAME AS FWD_COLUMN "
                    + "FROM ALL_CONSTRAINTS con "
                    + "INNER JOIN ALL_CONS_COLUMNS col ON col.CONSTRAINT_NAME = con.CONSTRAINT_NAME "
                    + "INNER JOIN ALL_CONS_COLUMNS col2 ON col2.CONSTRAINT_NAME = con.R_CONSTRAINT_NAME "
                    + "WHERE con.OWNER = '" + schema + "' AND col.OWNER = '" + schema + "' AND col2.OWNER = '" + schema + "' AND con.CONSTRAINT_TYPE = 'R'";
                break;
        }

        log.info("executing query " + query);

        List<RepoRelation> repoRelations = new ArrayList<>();
        try {
            var statement = this.connectionFactory.createStatement();
            if (statement.execute(query)) {
                var resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    var pwdTable = resultSet.getString("BWD_TABLE").toUpperCase();
                    var pwdColumn = resultSet.getString("BWD_COLUMN").toUpperCase();
                    var fwdTable = resultSet.getString("FWD_TABLE").toUpperCase();
                    var fwdColumn = resultSet.getString("FWD_COLUMN").toUpperCase();
                    repoRelations.add(new RepoRelation(pwdTable, pwdColumn, fwdTable, fwdColumn));
                }
                statement.getResultSet().close();
            }
            statement.close();
        } catch (SQLException exception) {
            var msg = "error reading all relations from db: " + exception.getMessage();
            log.severe(msg);
            throw new RepoException(msg, exception);
        }

        return repoRelations;
    }


    private List<RepoField> readAllRepoFields() throws RepoException {
        Set<String> pkTableColumns = new HashSet<>();
        Set<String> uniqueTableColumns = new HashSet<>();
        this.populatePkAndUniqueTableColumns(pkTableColumns, uniqueTableColumns);

        String query;
        switch (this.connectionFactory.getJdbcServerType()) {
            case MYSQL:
                query = "select TABLE_NAME, COLUMN_NAME, DATA_TYPE, IS_NULLABLE as NULLABLE "
                    + "from information_schema.columns "
                    + "where table_schema = DATABASE()";
                break;
            case ORACLE:
            default:
                var schema = this.connectionFactory.getCurrentSchema();
                query = "SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE, NULLABLE FROM ALL_TAB_COLUMNS WHERE OWNER = '" + schema + "'";
                break;
        }

        log.info("executing query " + query);

        List<RepoField> repoFields = new ArrayList<>();

        try {
            var statement = this.connectionFactory.createStatement();
            if (statement.execute(query)) {
                var resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    var tableName = resultSet.getString("TABLE_NAME").toUpperCase();
                    var colName = resultSet.getString("COLUMN_NAME").toUpperCase();
                    var dataType = resultSet.getString("DATA_TYPE").toUpperCase();
                    var isNullable = resultSet.getString("NULLABLE").toUpperCase();

                    var key = this.getTableColumnKey(tableName, colName);
                    var repoField = new RepoField(
                        tableName,
                        colName,
                        this.getRepoFieldType(dataType),
                        pkTableColumns.contains(key),
                        this.getIsNullable(isNullable),
                        uniqueTableColumns.contains(key)
                    );
                    repoFields.add(repoField);
                }
                statement.getResultSet().close();
            }
            statement.close();
        } catch (SQLException exception) {
            var msg = "error reading all columns from db: " + exception.getMessage();
            log.severe(msg);
            throw new RepoException(msg, exception);
        }

        return repoFields;
    }


    private void populatePkAndUniqueTableColumns(Set<String> pkTableColumns, Set<String> uniqueTableColumns) throws RepoException {
        String query;
        switch (this.connectionFactory.getJdbcServerType()) {
            case MYSQL:
                query = "select tc.TABLE_NAME, cu.COLUMN_NAME, tc.CONSTRAINT_TYPE "
                    + "from information_schema.table_constraints as tc "
                    + "inner join information_schema.key_column_usage as cu ON (cu.constraint_name = tc.constraint_name AND cu.table_name = tc.table_name) "
                    + "where tc.constraint_schema = DATABASE()";
                break;
            case ORACLE:
            default:
                var schema = this.connectionFactory.getCurrentSchema();
                query = "SELECT con.TABLE_NAME, col.COLUMN_NAME, con.CONSTRAINT_TYPE "
                    + "FROM ALL_CONSTRAINTS con "
                    + "INNER JOIN ALL_CONS_COLUMNS col ON (col.CONSTRAINT_NAME = con.CONSTRAINT_NAME AND col.TABLE_NAME = con.TABLE_NAME) "
                    + "WHERE con.OWNER = '" + schema + "' AND col.OWNER = '" + schema + "' AND con.CONSTRAINT_TYPE IN ('P', 'U')";
                break;
        }

        log.info("executing query " + query);

        try {
            var statement = this.connectionFactory.createStatement();
            if (statement.execute(query)) {
                var resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    var tableName = resultSet.getString("TABLE_NAME").toUpperCase();
                    var colName = resultSet.getString("COLUMN_NAME").toUpperCase();
                    var constraintType = resultSet.getString("CONSTRAINT_TYPE").toUpperCase();
                    var key = this.getTableColumnKey(tableName, colName);
                    if (this.isPkConstraint(constraintType)) {
                        pkTableColumns.add(key);
                    }
                    if (this.isUniqueConstraint(constraintType)) {
                        uniqueTableColumns.add(key);
                    }
                }
                statement.getResultSet().close();
            }
            statement.close();
        } catch (SQLException exception) {
            var msg = "error reading constraints from db: " + exception.getMessage();
            log.severe(msg);
            throw new RepoException(msg, exception);
        }
    }


    private boolean isPkConstraint(String constraintType) {
        switch (constraintType.toUpperCase()) {
            case "P":
            case "PRIMARY KEY":
                return true;
            default:
                return false;
        }
    }


    private boolean isUniqueConstraint(String constraintType) {
        switch (constraintType.toUpperCase()) {
            case "U":
            case "UNIQUE":
            case "P":
            case "PRIMARY KEY":
                return true;
            default:
                return false;
        }
    }


    private String getTableColumnKey(String tableName, String columnName) {
        return tableName + " " + columnName;
    }


    private RepoFieldType getRepoFieldType(String dataType) {
        switch (dataType.toUpperCase()) {
            case "INT":
            case "NUMBER":
                return RepoFieldType.LONG;
            case "DATE":
                return RepoFieldType.DATE;
            case "VARCHAR":
            case "VARCHAR2":
            default:
                return RepoFieldType.STRING;
        }
    }


    private boolean getIsNullable(String isNullable) {
        return isNullable.toUpperCase().charAt(0) == 'Y';
    }
}
