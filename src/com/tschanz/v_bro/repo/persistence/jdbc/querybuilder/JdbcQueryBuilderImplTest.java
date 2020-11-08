package com.tschanz.v_bro.repo.persistence.jdbc.querybuilder;

import com.tschanz.v_bro.repo.persistence.jdbc.mock.SpyConnectionFactory;
import com.tschanz.v_bro.repo.persistence.jdbc.model.FieldValue;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoFieldType;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcServerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class JdbcQueryBuilderImplTest {
    private SpyConnectionFactory spyConnectionFactory;
    private JdbcQueryBuilderImpl jdbcQueryBuilder;


    @BeforeEach
    void setUp() {
        this.spyConnectionFactory = new SpyConnectionFactory(false);
        this.jdbcQueryBuilder = new JdbcQueryBuilderImpl(this.spyConnectionFactory);
    }


    @Test
    void buildQuery_single_field_single_condition_oracle() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.ORACLE;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField( "ID", RepoFieldType.LONG, true,false, true));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, new ArrayList<>());

        assertEquals("select ID from TABLE1 where ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }


    @Test
    void buildQuery_multiple_fields_no_condition_mysql() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField("ID", RepoFieldType.LONG, true,false, true));
        fields.add(new RepoField("NAME", RepoFieldType.STRING, false, false, true));
        fields.add(new RepoField("ISACTIVE", RepoFieldType.BOOL, false, false, false));
        fields.add(new RepoField("CREATEDAT", RepoFieldType.DATE, false, true, false));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, new ArrayList<>());

        assertEquals("select ID,NAME,ISACTIVE,CREATEDAT from TABLE1 limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }


    @Test
    void buildQuery_single_equal_condition() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        List<RowFilter> filters = new ArrayList<>();
        RepoTable repoTable = new RepoTable("TABLE1", fields, Collections.emptyList(), Collections.emptyList());
        filters.add(new RowFilter(new RepoField("ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.EQUALS, 1L));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters);

        assertEquals("select ID from TABLE1 where ID=1 limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }


    @Test
    void buildQuery_single_in_conditionl() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        List<RowFilter> filters = new ArrayList<>();
        RepoTable repoTable = new RepoTable("TABLE1", fields, Collections.emptyList(), Collections.emptyList());
        filters.add(new RowFilter(new RepoField("ID", RepoFieldType.LONG, true, false, true), List.of(1L, 2L, 3L)));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters);

        assertEquals("select ID from TABLE1 where ID IN (1,2,3) limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }


    @Test
    void buildQuery_multiple_conditions() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.ORACLE;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        fields.add(new RepoField("NAME", RepoFieldType.STRING, false, false, true));
        List<RowFilter> filters = new ArrayList<>();
        RepoTable repoClass = new RepoTable("TABLE1", fields, Collections.emptyList(), Collections.emptyList());

        filters.add(new RowFilter(new RepoField("ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.GREATER_OR_EQUAL, 1L));
        filters.add(new RowFilter(new RepoField("ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.LESS_OR_EQUAL, 1000L));
        filters.add(new RowFilter(new RepoField("NAME", RepoFieldType.STRING, false, false, false), RowFilterOperator.EQUALS, "MEEP"));
        filters.add(new RowFilter(new RepoField("ISACTIVE", RepoFieldType.BOOL, false, false, false), RowFilterOperator.EQUALS, true));


        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters);

        assertEquals("select ID,NAME from TABLE1 where ID>=1 and ID<=1000 and NAME='MEEP' and ISACTIVE=1 and ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }


    @Test
    void buildQuery_date_condition_mysql() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        List<RowFilter> filters = new ArrayList<>();
        RepoTable repoClass = new RepoTable("TABLE1", fields, Collections.emptyList(), Collections.emptyList());
        filters.add(new RowFilter(new RepoField("CREATEDAT", RepoFieldType.DATE, false, true, false), RowFilterOperator.EQUALS, Date.valueOf(LocalDate.of(1976, 8, 28))));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters);

        assertEquals("select ID from TABLE1 where CREATEDAT=str_to_date('1976-08-28','%Y-%m-%d') limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }


    @Test
    void buildQuery_date_condition_oracle() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.ORACLE;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        List<RowFilter> filters = new ArrayList<>();
        filters.add(new RowFilter(new RepoField("CREATEDAT", RepoFieldType.DATE, false, false, false), RowFilterOperator.EQUALS, Date.valueOf(LocalDate.of(1976, 8, 28))));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters);

        assertEquals("select ID from TABLE1 where CREATEDAT=to_date('1976-08-28','YYYY-MM-DD') and ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }


    @Test
    void buildQuery_escape_quote() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        List<RepoField> fields = new ArrayList<>();
        fields.add(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        List<RowFilter> filters = new ArrayList<>();
        filters.add(new RowFilter(new RepoField("REMARK", RepoFieldType.STRING, false, true, false), RowFilterOperator.EQUALS, "it's old"));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters);

        assertEquals("select ID from TABLE1 where REMARK='it''s old' limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM, query);
    }
}
