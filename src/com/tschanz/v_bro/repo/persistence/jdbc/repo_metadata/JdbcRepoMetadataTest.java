package com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata;/*package com.tschanz.v_bro.versioning_structure.data.jdbc.testing;

import com.tschanz.v_bro.repo_common.domain.RepoException;
import com.tschanz.v_bro.repo_common.testing.MockDatabaseMetaData;
import com.tschanz.v_bro.repo_common.testing.MockResult;
import com.tschanz.v_bro.repo_common.testing.MockResultSet;
import com.tschanz.v_bro.repo_connection.data.jdbc.testing.MockConnectionFactory;
import com.tschanz.v_bro.versioning_structure.data.jdbc.JdbcRepoMetadata;
import com.tschanz.v_bro.versioning_structure.data.jdbc.JdbcVersioningStructure;
import com.tschanz.v_bro.versioning_structure.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class JdbcRepoMetadataTest {
    private MockDatabaseMetaData mockDatabaseMetaData;
    private JdbcRepoMetadata jdbcRepoMetadata;


    @BeforeEach
    void setUp() {
        MockConnectionFactory mockConnectionFactory = new MockConnectionFactory(true);
        this.mockDatabaseMetaData = mockConnectionFactory.mockConnection.mockDatabaseMetaData;
        this.jdbcRepoMetadata = new JdbcRepoMetadata(mockConnectionFactory);
    }


    // region findTablesNamesBySuffix

    @Test
    void findTablesNamesBySuffix_happy_day() throws RepoException {
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{ "TABLE_NAME", "TABLE1" }}),
            MockResult.fromStrings(new String[][] {{ "TABLE_NAME", "TABLE2" }}),
            MockResult.fromStrings(new String[][] {{ "TABLE_NAME", "TABLE3" }})
        ));

        List<String> tableNames = this.jdbcRepoMetadata.findElementClasses("TAB");

        assertEquals(3, tableNames.size());
        assertEquals("TABLE1", tableNames.get(0));
        assertEquals("TABLE2", tableNames.get(1));
        assertEquals("TABLE3", tableNames.get(2));
    }


    @Test
    void findTablesNamesBySuffix_no_results() throws RepoException {
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.emptyResultSet());

        List<String> tableNames = this.jdbcRepoMetadata.findElementClasses("TAB");

        assertEquals(0, tableNames.size());
    }


    @Test
    void findTablesNamesBySuffix_sql_exception() {
        this.mockDatabaseMetaData.mockHelper.setThrowException(new SQLException("MEEP"));

        Exception exception = assertThrows(RepoException.class, () -> {
            this.jdbcRepoMetadata.findElementClasses("TAB");
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }


    @Test
    void findTablesNamesBySuffix_escapes_underscore() throws RepoException {
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.emptyResultSet());
        this.mockDatabaseMetaData.searchStringEscape = "$";

        this.jdbcRepoMetadata.findElementClasses("TABLE1_E");

        Assertions.assertTrue(this.mockDatabaseMetaData.mockHelper.isMethodCalled("getSearchStringEscape"));
        Assertions.assertTrue(this.mockDatabaseMetaData.mockHelper.isMethodCalled("getTables"));
        Assertions.assertEquals(JdbcRepoMetadata.WILDCARD + "TABLE1$_E", this.mockDatabaseMetaData.mockHelper.getMethodArgument("getTables", 2));
    }

    // endregion


    // region readTableStructure

    @Test
    void readTableStructure_minimal_table() throws RepoException {
        String tableName = "TABLE1_E";
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", tableName}, {"TABLE_SCHEM ", null}, {"TABLE_TYPE", "TABLE"}})
        )); // table
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", tableName}, {"COLUMN_NAME", "ID"}, {"DATA_TYPE", Types.BIGINT + ""}, {"NULLABLE", DatabaseMetaData.columnNoNulls + ""}})
        )); // columns
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", tableName}, {"COLUMN_NAME", "ID"}, {"PK_NAME", "IDX_ID"}})
        )); // pk
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.emptyResultSet()); // index
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.emptyResultSet()); // rel out
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.emptyResultSet()); // rel in


        RepoClass result = this.jdbcRepoMetadata.readClass(tableName);

        assertEquals(tableName, result.getName());
        assertEquals(1, result.getColumns().size());
        assertEquals(1, result.getPrimaryKeys().size());
        assertEquals(0, result.getIndexes().size());
        assertEquals(0, result.getOutgoingRelations().size());
        assertEquals(0, result.getIncomingRelations().size());
    }


    @Test
    void readTableStructure_table_with_everything() throws RepoException {
        String tableName = "TABLE1_E";
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", tableName}, {"TABLE_SCHEM ", null}, {"TABLE_TYPE", "TABLE"}})
        )); // table
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", tableName}, {"COLUMN_NAME", "ID"}, {"DATA_TYPE", Types.BIGINT + ""}, {"NULLABLE", DatabaseMetaData.columnNoNulls + ""}}),
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", tableName}, {"COLUMN_NAME", "NAME"}, {"DATA_TYPE", Types.NVARCHAR + ""}, {"NULLABLE", DatabaseMetaData.columnNullable + ""}}),
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", tableName}, {"COLUMN_NAME", "XYZ"}, {"DATA_TYPE", Types.NVARCHAR + ""}, {"NULLABLE", DatabaseMetaData.columnNullable + ""}})
        )); // columns
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", tableName}, {"COLUMN_NAME", "ID"}, {"PK_NAME", "IDX_ID"}})
        )); // pk
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", tableName}, {"COLUMN_NAME", "ID"}, {"INDEX_NAME", "PK"}, {"NON_UNIQUE", "0"}}),
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", tableName}, {"COLUMN_NAME", "NAME"}, {"INDEX_NAME", "LINK_TO_ELEMENT"}, {"NON_UNIQUE", "0"}})
        )); // index
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"FKTABLE_NAME", tableName}, {"FKCOLUMN_NAME", "ID_TABLE2"}, {"PKTABLE_NAME", "TABLE2_E"}, {"PKCOLUMN_NAME", "ID"}})
        )); // rel out
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"FKTABLE_NAME", "TABLE1_V"}, {"FKCOLUMN_NAME", "ID_ELEMENT"}, {"PKTABLE_NAME", tableName}, {"PKCOLUMN_NAME", "ID"}}),
            MockResult.fromStrings(new String[][] {{"FKTABLE_NAME", "TABLE2_V"}, {"FKCOLUMN_NAME", "ID_TABLE1"}, {"PKTABLE_NAME", tableName}, {"PKCOLUMN_NAME", "ID"}})
        )); // rel in


        RepoClass result = this.jdbcRepoMetadata.readClass(tableName);

        assertEquals(tableName, result.getName());
        assertEquals(3, result.getColumns().size());
        assertEquals(1, result.getPrimaryKeys().size());
        assertEquals(2, result.getIndexes().size());
        assertEquals(1, result.getOutgoingRelations().size());
        assertEquals(2, result.getIncomingRelations().size());
    }
    @Test
    void readTableStructure_sql_exception() {
        String tableName = "TABLE1_V";
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.throwErrorResultSet(new SQLException("MEEP")));

        Exception exception = assertThrows(RepoException.class, () -> {
            this.jdbcRepoMetadata.readTable(tableName);
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }

    // endregion


    // region readTable

    @Test
    void readTable_happy_day() throws RepoException {
        String tableName = "TABLE1_V";
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", tableName}, {"TABLE_SCHEM ", null}, {"TABLE_TYPE", "TABLE"}})
        ));

        String result = this.jdbcRepoMetadata.readTable(tableName);

        assertEquals(tableName, result);
    }


    @Test
    void readTable_table_not_found() {
        String tableName = "TABLE1_V";
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.emptyResultSet());

        assertThrows(RepoException.class, () -> {
            this.jdbcRepoMetadata.readTable(tableName);
        });
    }


    @Test
    void readTable_sql_exception() {
        String tableName = "TABLE1_V";
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.throwErrorResultSet(new SQLException("MEEP")));

        Exception exception = assertThrows(RepoException.class, () -> {
            this.jdbcRepoMetadata.readTable(tableName);
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }

    // endregion


    // region readColumns

    @Test
    void readColumns_happy_day() throws RepoException {
        String tableName = "TABLE1_E";
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", tableName}, {"COLUMN_NAME", "ID"}, {"DATA_TYPE", Types.BIGINT + ""}, {"NULLABLE", DatabaseMetaData.columnNoNulls + ""}}),
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", tableName}, {"COLUMN_NAME", "NAME"}, {"DATA_TYPE", Types.NVARCHAR + ""}, {"NULLABLE", DatabaseMetaData.columnNullable + ""}})
        ));

        List<ColumnInfo> columnInfos = this.jdbcRepoMetadata.readFields(tableName);

        assertEquals(2, columnInfos.size());
        assertEquals("ID", columnInfos.get(0).getColumnName());
        assertEquals(RepoFieldType.LONG, columnInfos.get(0).getFieldType());
        assertEquals("NAME", columnInfos.get(1).getColumnName());
        assertEquals(RepoFieldType.STRING, columnInfos.get(1).getFieldType());
    }


    @Test
    void readColumns_no_columns() throws RepoException {
        String tableName = "TABLE1_E";
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.emptyResultSet());

        List<ColumnInfo> columnInfos = this.jdbcRepoMetadata.readFields(tableName);

        assertEquals(0, columnInfos.size());
    }


    @Test
    void readColumns_sql_exception() throws RepoException {
        String tableName = "TABLE1_E";
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.throwErrorResultSet(new SQLException("MEEP")));

        Exception exception = assertThrows(RepoException.class, () -> {
            this.jdbcRepoMetadata.readFields(tableName);
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }

    // endregion


    // region readColumns

    @Test
    void readPrimaryKeys_happy_day() throws RepoException {
        String tableName = "TABLE1_E";
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", tableName}, {"COLUMN_NAME", "ID"}, {"PK_NAME", "IDX_ID"}})
        ));

        List<PkInfo> pkInfos = this.jdbcRepoMetadata.readPrimaryKeys(tableName);

        assertEquals(1, pkInfos.size());
        assertEquals("ID", pkInfos.get(0).getColumnName());
        assertEquals("IDX_ID", pkInfos.get(0).getPkName());
    }


    @Test
    void readPrimaryKeys_no_columns() throws RepoException {
        String tableName = "TABLE1_E";
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.emptyResultSet());

        List<PkInfo> pkInfos = this.jdbcRepoMetadata.readPrimaryKeys(tableName);

        assertEquals(0, pkInfos.size());
    }


    @Test
    void readPrimaryKeys_sql_exception() throws RepoException {
        String tableName = "TABLE1_E";
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.throwErrorResultSet(new SQLException("MEEP")));

        Exception exception = assertThrows(RepoException.class, () -> {
            this.jdbcRepoMetadata.readPrimaryKeys(tableName);
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }

    // endregion


    // region readIndexes

    @Test
    void readIndexes_outgoing() throws RepoException {
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", "TABLE1_V"}, {"COLUMN_NAME", "ID"}, {"INDEX_NAME", "PK"}, {"NON_UNIQUE", "0"}}),
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", "TABLE1_V"}, {"COLUMN_NAME", "ID_ELEMENT"}, {"INDEX_NAME", "LINK_TO_ELEMENT"}, {"NON_UNIQUE", "0"}})
        ));

        List<IndexInfo> indexInfos = this.jdbcRepoMetadata.readIndexes("TABLE1_V", false);

        assertEquals(2, indexInfos.size());
        assertEquals("TABLE1_V", indexInfos.get(0).getTableName());
        assertEquals("ID", indexInfos.get(0).getColumnName());
        assertEquals("PK", indexInfos.get(0).getIndexName());
        assertTrue(indexInfos.get(0).isUnique());
        assertEquals("TABLE1_V", indexInfos.get(1).getTableName());
        assertEquals("ID_ELEMENT", indexInfos.get(1).getColumnName());
        assertEquals("LINK_TO_ELEMENT", indexInfos.get(1).getIndexName());
        assertTrue(indexInfos.get(1).isUnique());
    }


    @Test
    void readIndexes_no_results() throws RepoException {
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.emptyResultSet());

        List<IndexInfo> indexInfos = this.jdbcRepoMetadata.readIndexes("TABLE1_V", true);

        assertEquals(0, indexInfos.size());
    }


    @Test
    void readIndexes_sql_exception() {
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.throwErrorResultSet(new SQLException("MEEP")));

        Exception exception = assertThrows(RepoException.class, () -> {
            this.jdbcRepoMetadata.readIndexes("TABLE1_V", true);
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }


    @Test
    void readIndexes_index_with_null_name() throws RepoException {
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", "TABLE1_V"}, {"COLUMN_NAME", "ID"}, {"INDEX_NAME", "PK"}, {"NON_UNIQUE", "0"}}),
            MockResult.fromStrings(new String[][] {{"TABLE_NAME", "TABLE1_V"}, {"COLUMN_NAME", null}, {"INDEX_NAME", null}, {"NON_UNIQUE", null}})
        ));

        List<IndexInfo> indexInfos = this.jdbcRepoMetadata.readIndexes("TABLE1_V", true);

        assertEquals(1, indexInfos.size());
        assertEquals("PK", indexInfos.get(0).getIndexName());
    }

    // endregion


    // region readRelations

    @Test
    void readRelations_incoming() throws RepoException {
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"FKTABLE_NAME", "TABLE1_V"}, {"FKCOLUMN_NAME", "ID_ELEMENT"}, {"PKTABLE_NAME", "TABLE1_E"}, {"PKCOLUMN_NAME", "ID"}}),
            MockResult.fromStrings(new String[][] {{"FKTABLE_NAME", "TABLE2_V"}, {"FKCOLUMN_NAME", "ID_TABLE1"}, {"PKTABLE_NAME", "TABLE1_E"}, {"PKCOLUMN_NAME", "ID"}})
        ));

        List<RepoRelation> repoRelations = this.jdbcRepoMetadata.readRelations("TABLE1_E", true);

        assertEquals(2, repoRelations.size());
        assertEquals("TABLE1_V", repoRelations.get(0).getBwdClassName());
        assertEquals("ID_ELEMENT", repoRelations.get(0).getBwdFieldName());
        assertEquals("TABLE1_E", repoRelations.get(0).getFwdClassName());
        assertEquals("ID", repoRelations.get(0).getFwdFieldName());
        assertEquals("TABLE2_V", repoRelations.get(1).getBwdClassName());
        assertEquals("ID_TABLE1", repoRelations.get(1).getBwdFieldName());
        assertEquals("TABLE1_E", repoRelations.get(1).getFwdClassName());
        assertEquals("ID", repoRelations.get(1).getFwdFieldName());
    }


    @Test
    void readRelations_outgoing() throws RepoException {
        this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{"FKTABLE_NAME", "TABLE1_V"}, {"FKCOLUMN_NAME", "ID_ELEMENT"}, {"PKTABLE_NAME", "TABLE1_E"}, {"PKCOLUMN_NAME", "ID"}})
        ));

        List<RepoRelation> repoRelations = this.jdbcRepoMetadata.readRelations("TABLE1_E", false);

        assertEquals(1, repoRelations.size());
        assertEquals("TABLE1_V", repoRelations.get(0).getBwdClassName());
        assertEquals("ID_ELEMENT", repoRelations.get(0).getBwdFieldName());
        assertEquals("TABLE1_E", repoRelations.get(0).getFwdClassName());
        assertEquals("ID", repoRelations.get(0).getFwdFieldName());
    }


    @Test
    void readRelations_no_results() throws RepoException {
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.emptyResultSet());

        List<RepoRelation> repoRelations = this.jdbcRepoMetadata.readRelations("TABLE1_E", true);

        assertEquals(0, repoRelations.size());
    }


    @Test
    void readRelations_sql_exception() throws RepoException {
        this.mockDatabaseMetaData.mockResultSets.add(MockResultSet.throwErrorResultSet(new SQLException("MEEP")));

        Exception exception = assertThrows(RepoException.class, () -> {
            this.jdbcRepoMetadata.readRelations("TABLE1_E", true);
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }

    // endregion
}
*/