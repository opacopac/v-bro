package com.tschanz.v_bro.repo.persistence.jdbc.querybuilder;

import com.tschanz.v_bro.repo.persistence.jdbc.mock.MockConnectionFactory;
import com.tschanz.v_bro.repo.persistence.jdbc.model.FieldValue;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoFieldType;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class JdbcQueryBuilderImplTest {
    private MockConnectionFactory mockConnectionFactory;
    private JdbcQueryBuilderImpl jdbcQueryBuilder;


    @BeforeEach
    void setUp() {
        this.mockConnectionFactory = new MockConnectionFactory(false);
        this.jdbcQueryBuilder = new JdbcQueryBuilderImpl(this.mockConnectionFactory);
    }


    @Test
    void buildQuery_single_field_oracle() {
        this.mockConnectionFactory.isCurrentConnectionMySqlResult = false;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField( "ID", RepoFieldType.LONG, true,false, true));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, new ArrayList<>());

        assertEquals("select TABLE1.ID from TABLE1 where ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }


    @Test
    void buildQuery_multiple_fields_mysql() {
        this.mockConnectionFactory.isCurrentConnectionMySqlResult = true;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField("ID", RepoFieldType.LONG, true,false, true));
        fields.add(new RepoField("NAME", RepoFieldType.STRING, false, false, true));
        fields.add(new RepoField("ISACTIVE", RepoFieldType.BOOL, false, false, false));
        fields.add(new RepoField("CREATEDAT", RepoFieldType.DATE, false, true, false));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, new ArrayList<>());

        assertEquals("select TABLE1.ID,TABLE1.NAME,TABLE1.ISACTIVE,TABLE1.CREATEDAT from TABLE1 limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }


    @Test
    void buildQuery_single_condition_mysql() {
        this.mockConnectionFactory.isCurrentConnectionMySqlResult = true;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        List<RowFilter> filters = new ArrayList<>();
        RepoTable repoTable = new RepoTable("TABLE1", fields, Collections.emptyList(), Collections.emptyList());
        filters.add(new RowFilter(RowFilterOperator.EQUALS, new FieldValue(new RepoField("ID", RepoFieldType.LONG, true, false, true), 1L)));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters);

        assertEquals("select TABLE1.ID from TABLE1 where TABLE1.ID=1 limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }


/*    @Test
    void buildQuery_multiple_conditions_oracle() {
        this.mockConnectionFactory.isCurrentConnectionMySqlResult = false;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        fields.add(new RepoField("NAME", RepoFieldType.STRING, false, false, true));
        List<RowFilter> filters = new ArrayList<>();
        RepoClass repoClass = new RepoClass("TABLE1", fields, Collections.emptyList(), Collections.emptyList());

        filters.add(new RowFilter(RowFilterOperator.GREATER_OR_EQUAL, new FieldValue(new RepoField(new ColumnInfo("TABLE1", "ID", RepoFieldType.LONG, true)), 1L)));
        filters.add(new RowFilter(RowFilterOperator.LESS_OR_EQUAL, new FieldValue(new RepoField(new ColumnInfo("TABLE1", "ID", RepoFieldType.LONG, true)), 1000L)));
        filters.add(new RowFilter(RowFilterOperator.EQUALS, new FieldValue(new RepoField(new ColumnInfo("TABLE1", "NAME", RepoFieldType.STRING, true)), "MEEP")));
        filters.add(new RowFilter(RowFilterOperator.EQUALS, new FieldValue(new RepoField(new ColumnInfo("TABLE1", "ISACTIVE", RepoFieldType.BOOL, true)), true)));


        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters);

        assertEquals("select TABLE1.ID,TABLE1.NAME from TABLE1 where TABLE1.ID>=1 and TABLE1.ID<=1000 and TABLE1.NAME='MEEP' and TABLE1.ISACTIVE=1 and ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }


    @Test
    void buildQuery_date_condition_mysql() {
        this.mockConnectionFactory.isCurrentConnectionMySqlResult = true;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        List<RowFilter> filters = new ArrayList<>();
        RepoClass repoClass = new RepoClass("TABLE1", fields, Collections.emptyList(), Collections.emptyList());
        filters.add(new RowFilter(RowFilterOperator.EQUALS, new FieldValue(repoClass, new RepoField("CREATEDAT", RepoFieldType.DATE, false, true, false)), new Date(1976-1900, Calendar.AUGUST, 28))));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters);

        assertEquals("select TABLE1.ID from TABLE1 where TABLE1.CREATEDAT=str_to_date('1976-08-28','%Y-%m-%d') limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }


    @Test
    void buildQuery_date_condition_oracle() {
        this.mockConnectionFactory.isCurrentConnectionMySqlResult = false;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        List<RowFilter> filters = new ArrayList<>();
        filters.add(new RowFilter(RowFilterOperator.EQUALS, new FieldValue(new RepoField(new ColumnInfo("TABLE1", "CREATEDAT", RepoFieldType.DATE, true)), new Date(1976-1900, Calendar.AUGUST, 28))));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters);

        assertEquals("select TABLE1.ID from TABLE1 where TABLE1.CREATEDAT=to_date('1976-08-28','YYYY-MM-DD') and ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }


    @Test
    void buildQuery_escape_quote() {
        this.mockConnectionFactory.isCurrentConnectionMySqlResult = true;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        List<RowFilter> filters = new ArrayList<>();
        filters.add(new RowFilter(RowFilterOperator.EQUALS, new FieldValue(new RepoField(new ColumnInfo("TABLE1", "REMARK", RepoFieldType.STRING, true)), "it's old")));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters);

        assertEquals("select TABLE1.ID from TABLE1 where TABLE1.REMARK='it''s old' limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }*/
}
