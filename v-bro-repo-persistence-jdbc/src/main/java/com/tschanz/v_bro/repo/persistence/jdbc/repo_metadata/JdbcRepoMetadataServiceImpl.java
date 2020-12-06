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
    public List<String> findTableNames(String tableNamePattern) throws RepoException {
        try {
            ResultSet tablesResult = this.connectionFactory.getCurrentConnection().getMetaData().getTables(
                null,
                null,
                tableNamePattern,
                new String[]{"TABLE"}
            );

            List<String> tableNames = new ArrayList<>();
            while (tablesResult.next()) {
                String name = tablesResult.getString("TABLE_NAME").toUpperCase();
                tableNames.add(name);
            }
            tablesResult.close();

            return tableNames;
        } catch (SQLException exception) {
            String msg = "error reading element structure: " + exception.getMessage();
            log.severe(msg);
            throw new RepoException(msg, exception);
        }
    }


    @Override
    public RepoTable readTableStructure(String tableName) throws RepoException {
        if (this.repoFieldLut == null) {
            this.repoFieldLut = this.readAllRepoFields();
        }

        if (this.repoRelationLut == null) {
            this.repoRelationLut = this.readAllRelations();
        }

        List<RepoField> repoFields = this.repoFieldLut
            .stream()
            .filter(field -> field.tableName.equals(tableName.toUpperCase()))
            .collect(Collectors.toList());
        List<RepoRelation> outgoingRelations = this.repoRelationLut
            .stream()
            .filter(relation -> relation.getBwdClassName().equals(tableName.toUpperCase()))
            .collect(Collectors.toList());
        List<RepoRelation> incomingRelations = this.repoRelationLut
            .stream()
            .filter(relation -> relation.getFwdClassName().equals(tableName.toUpperCase()))
            .collect(Collectors.toList());

        return new RepoTable(
            tableName.toUpperCase(),
            repoFields,
            outgoingRelations,
            incomingRelations
        );
    }


    @Override
    public String escapeUnderscore(String tableNamePattern) throws RepoException {
        try {
            String escapeChar = this.connectionFactory.getCurrentConnection().getMetaData().getSearchStringEscape();
            return tableNamePattern.replace("_", escapeChar + "_");
        } catch (SQLException exception) {
            String msg = "error reading wildcard from repo_metadata: " + exception.getMessage();
            log.severe(msg);
            throw new RepoException(msg, exception);
        }
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
                query = "SELECT uc.TABLE_NAME AS BWD_TABLE, col.COLUMN_NAME AS BWD_COLUMN, col2.TABLE_NAME AS FWD_TABLE, col2.COLUMN_NAME AS FWD_COLUMN "
                    + "FROM USER_CONSTRAINTS uc "
                    + "INNER JOIN USER_CONS_COLUMNS col ON col.CONSTRAINT_NAME = uc.CONSTRAINT_NAME "
                    + "INNER JOIN USER_CONS_COLUMNS col2 ON col2.CONSTRAINT_NAME = uc.R_CONSTRAINT_NAME "
                    + "WHERE uc.CONSTRAINT_TYPE = 'R'";
                break;
        }

        log.info("executing query " + query);

        List<RepoRelation> repoRelations = new ArrayList<>();
        try {
            Statement statement = this.connectionFactory.getCurrentConnection().createStatement();
            if (statement.execute(query)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String pwdTable = resultSet.getString("BWD_TABLE").toUpperCase();
                    String pwdColumn = resultSet.getString("BWD_COLUMN").toUpperCase();
                    String fwdTable = resultSet.getString("FWD_TABLE").toUpperCase();
                    String fwdColumn = resultSet.getString("FWD_COLUMN").toUpperCase();
                    repoRelations.add(new RepoRelation(pwdTable, pwdColumn, fwdTable, fwdColumn));
                }
                statement.getResultSet().close();
            }
            statement.close();
        } catch (SQLException exception) {
            String msg = "error reading all relations from db: " + exception.getMessage();
            log.severe(msg);
            throw new RepoException(msg, exception);
        }

        return repoRelations;
    }


    private List<RepoField> readAllRepoFields() throws RepoException {
        Set<String> idTableColumns = new HashSet<>();
        Set<String> uniqueTableColumns = new HashSet<>();
        this.populateIdAndUniqueTableColumns(idTableColumns, uniqueTableColumns);

        String query;
        switch (this.connectionFactory.getJdbcServerType()) {
            case MYSQL:
                query = "select TABLE_NAME, COLUMN_NAME, DATA_TYPE, IS_NULLABLE as NULLABLE "
                    + "from information_schema.columns "
                    + "where table_schema = DATABASE()";
                break;
            case ORACLE:
            default:
                query = "SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE, NULLABLE FROM USER_TAB_COLUMNS";
                break;
        }

        log.info("executing query " + query);

        List<RepoField> repoFields = new ArrayList<>();

        try {
            Statement statement = this.connectionFactory.getCurrentConnection().createStatement();
            if (statement.execute(query)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String tableName = resultSet.getString("TABLE_NAME").toUpperCase();
                    String colName = resultSet.getString("COLUMN_NAME").toUpperCase();
                    String dataType = resultSet.getString("DATA_TYPE").toUpperCase();
                    String isNullable = resultSet.getString("NULLABLE").toUpperCase();

                    String key = this.getTableColumnKey(tableName, colName);
                    RepoField repoField = new RepoField(
                        colName,
                        this.getRepoFieldType(dataType),
                        idTableColumns.contains(key),
                        this.getIsNullable(isNullable),
                        uniqueTableColumns.contains(key)
                    );
                    repoField.tableName = tableName;
                    repoFields.add(repoField);
                }
                statement.getResultSet().close();
            }
            statement.close();
        } catch (SQLException exception) {
            String msg = "error reading all columns from db: " + exception.getMessage();
            log.severe(msg);
            throw new RepoException(msg, exception);
        }

        return repoFields;
    }


    private void populateIdAndUniqueTableColumns(Set<String> idTableColumns, Set<String> uniqueTableColumns) throws RepoException {
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
                query = "SELECT uc.TABLE_NAME, col.COLUMN_NAME, uc.CONSTRAINT_TYPE "
                    + "FROM USER_CONSTRAINTS uc "
                    + "INNER JOIN USER_CONS_COLUMNS col ON (col.CONSTRAINT_NAME = uc.CONSTRAINT_NAME AND col.TABLE_NAME = uc.TABLE_NAME) "
                    + "WHERE uc.CONSTRAINT_TYPE IN ('P', 'U')";
                break;
        }

        log.info("executing query " + query);

        try {
            Statement statement = this.connectionFactory.getCurrentConnection().createStatement();
            if (statement.execute(query)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String tableName = resultSet.getString("TABLE_NAME").toUpperCase();
                    String colName = resultSet.getString("COLUMN_NAME").toUpperCase();
                    String constraintType = resultSet.getString("CONSTRAINT_TYPE").toUpperCase();
                    String key = this.getTableColumnKey(tableName, colName);
                    if (this.isIdConstraint(constraintType)) {
                        idTableColumns.add(key);
                    }
                    if (this.isUniqueConstraint(constraintType)) {
                        uniqueTableColumns.add(key);
                    }
                }
                statement.getResultSet().close();
            }
            statement.close();
        } catch (SQLException exception) {
            String msg = "error reading constraints from db: " + exception.getMessage();
            log.severe(msg);
            throw new RepoException(msg, exception);
        }
    }


    private boolean isIdConstraint(String constraintType) {
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
