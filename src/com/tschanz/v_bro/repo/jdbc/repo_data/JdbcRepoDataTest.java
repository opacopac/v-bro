/*package com.tschanz.v_bro.repo.jdbc.repo_data;

import com.tschanz.v_bro.common.KeyValue;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.jdbc.mock.MockConnectionFactory;
import com.tschanz.v_bro.repo.jdbc.mock.MockQueryBuilder;
import com.tschanz.v_bro.repo.jdbc.mock.MockResult;
import com.tschanz.v_bro.repo.jdbc.mock.MockResultSet;
import com.tschanz.v_bro.repo.jdbc.model.FieldValue;
import com.tschanz.v_bro.repo.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.jdbc.model.RepoFieldType;
import com.tschanz.v_bro.repo.jdbc.model.RowInfo;
import com.tschanz.v_bro.repo.jdbc.querybuilder.RowFilter;
import com.tschanz.v_bro.repo.jdbc.querybuilder.RowFilterOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class JdbcRepoDataTest {
    private MockConnectionFactory mockConnectionFactory;
    private JdbcRepoData jdbcRepoReadRows;
    private MockQueryBuilder mockQueryBuilder;


    @BeforeEach
    void setUp() {
        this.mockConnectionFactory = new MockConnectionFactory(true);
        this.mockQueryBuilder = new MockQueryBuilder();
        this.jdbcRepoReadRows = new JdbcRepoData(mockConnectionFactory, this.mockQueryBuilder);
    }


    @Test
    void readRows_happy_day_long_string() throws RepoException {
        this.mockQueryBuilder.query = "select ID,NAME from TABLE1 where ID > 1";
        this.mockConnectionFactory.mockConnection.mockStatement.mockResultSets.add(new MockResultSet(
            new MockResult(new KeyValue("ID", "123"), new KeyValue("NAME", "MEEP")),
            new MockResult(new KeyValue("ID", "456"), new KeyValue("NAME", "MAAP"))
        ));
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField("ID", RepoFieldType.LONG, true, false, true));
        fields.add(new RepoField("NAME", RepoFieldType.STRING, false, false, false));
        List<RowFilter> filters = new ArrayList<>();
        filters.add(new RowFilter(RowFilterOperator.GREATER_OR_EQUAL, new FieldValue(new RepoField(new ColumnInfo("TABLE1", "ID", RepoFieldType.LONG, false)), 1)));

        List<RowInfo> rowInfoList = this.jdbcRepoReadRows.readData("TABLE1", fields, filters);

        assertEquals(2, rowInfoList.size());
        assertEquals(2, rowInfoList.get(0).getFieldValueList().size());
        assertEquals("ID", rowInfoList.get(0).getFieldValueList().get("ID").getName());
        assertEquals(123L, rowInfoList.get(0).getFieldValueList().get("ID").getValue());
        assertEquals(RepoFieldType.LONG, rowInfoList.get(0).getFieldValueList().get("ID").getType());
        assertEquals("NAME", rowInfoList.get(0).getFieldValueList().get("NAME").getName());
        assertEquals("MEEP", rowInfoList.get(0).getFieldValueList().get("NAME").getValue());
        assertEquals(RepoFieldType.STRING, rowInfoList.get(0).getFieldValueList().get("NAME").getType());
        assertEquals(2, rowInfoList.get(1).getFieldValueList().size());
        assertEquals("ID", rowInfoList.get(1).getFieldValueList().get("ID").getName());
        assertEquals(456L, rowInfoList.get(1).getFieldValueList().get("ID").getValue());
        assertEquals(RepoFieldType.LONG, rowInfoList.get(1).getFieldValueList().get("ID").getType());
        assertEquals("NAME", rowInfoList.get(1).getFieldValueList().get("NAME").getName());
        assertEquals("MAAP", rowInfoList.get(1).getFieldValueList().get("NAME").getValue());
        assertEquals(RepoFieldType.STRING, rowInfoList.get(1).getFieldValueList().get("NAME").getType());
    }


    @Test
    void readRows_happy_day_bool_date() throws RepoException {
        this.mockQueryBuilder.query = "select ISACTIVE,CREATEDAT from TABLE1";
        this.mockConnectionFactory.mockConnection.mockStatement.mockResultSets.add(new MockResultSet(
            new MockResult(new KeyValue("ISACTIVE", "true"), new KeyValue("CREATEDAT", "2020-01-01")),
            new MockResult(new KeyValue("ISACTIVE", "false"), new KeyValue("CREATEDAT", "2018-12-31"))
        ));
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField(new ColumnInfo("TABLE1", "ISACTIVE", RepoFieldType.BOOL, false)));
        fields.add(new RepoField(new ColumnInfo("TABLE1", "CREATEDAT", RepoFieldType.DATE, true)));

        List<RowInfo> rowInfoList = this.jdbcRepoReadRows.readData("TABLE1", fields, new ArrayList<>());

        assertEquals(2, rowInfoList.size());
        assertEquals(2, rowInfoList.get(0).getFieldValueList().size());
        assertEquals("ISACTIVE", rowInfoList.get(0).getFieldValueList().get("ISACTIVE").getName());
        assertEquals(true, rowInfoList.get(0).getFieldValueList().get("ISACTIVE").getValue());
        assertEquals(RepoFieldType.BOOL, rowInfoList.get(0).getFieldValueList().get("ISACTIVE").getType());
        assertEquals("CREATEDAT", rowInfoList.get(0).getFieldValueList().get("CREATEDAT").getName());
        assertEquals(new Date(2020-1900, 0, 1), rowInfoList.get(0).getFieldValueList().get("CREATEDAT").getValue());
        assertEquals(RepoFieldType.DATE, rowInfoList.get(0).getFieldValueList().get("CREATEDAT").getType());
        assertEquals(2, rowInfoList.get(1).getFieldValueList().size());
        assertEquals("ISACTIVE", rowInfoList.get(1).getFieldValueList().get("ISACTIVE").getName());
        assertEquals(false, rowInfoList.get(1).getFieldValueList().get("ISACTIVE").getValue());
        assertEquals(RepoFieldType.BOOL, rowInfoList.get(1).getFieldValueList().get("ISACTIVE").getType());
        assertEquals("CREATEDAT", rowInfoList.get(1).getFieldValueList().get("CREATEDAT").getName());
        assertEquals(new Date(2018-1900, 11, 31), rowInfoList.get(1).getFieldValueList().get("CREATEDAT").getValue());
        assertEquals(RepoFieldType.DATE, rowInfoList.get(1).getFieldValueList().get("CREATEDAT").getType());
    }


    @Test
    void readRelations_no_results() throws RepoException {
        this.mockQueryBuilder.query = "select ISACTIVE,CREATEDAT from TABLE1";
        this.mockConnectionFactory.mockConnection.mockStatement.mockResultSets.add(MockResultSet.emptyResultSet());
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField(new ColumnInfo("TABLE1", "ID", RepoFieldType.LONG, false)));

        List<RowInfo> rowInfoList = this.jdbcRepoReadRows.readData("TABLE1", fields, new ArrayList<>());

        assertEquals(0, rowInfoList.size());
    }


    @Test
    void readRelations_sql_exception() {
        this.mockConnectionFactory.mockConnection.mockStatement.mockResultSets.add(MockResultSet.throwErrorResultSet(new SQLException("MEEP")));
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField(new ColumnInfo("TABLE1", "ID", RepoFieldType.LONG, false)));

        Exception exception = assertThrows(RepoException.class, () -> {
            this.jdbcRepoReadRows.readData("TABLE1", fields, new ArrayList<>());
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }
}*/
