package com.tschanz.v_bro.repo.persistence.jdbc.querybuilder;

import com.tschanz.v_bro.repo.persistence.jdbc.mock.SpyConnectionFactory;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoFieldType;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcServerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
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
        var fields = List.of(new RepoField( "ID", RepoFieldType.LONG, true,false, true));

        var query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, Collections.emptyList(), Collections.emptyList(), -1);

        assertEquals("select ID from TABLE1 where ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_multiple_fields_no_condition_mysql() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields = List.of(
            new RepoField("ID", RepoFieldType.LONG, true,false, true),
            new RepoField("NAME", RepoFieldType.STRING, false, false, true),
            new RepoField("ISACTIVE", RepoFieldType.BOOL, false, false, false),
            new RepoField("CREATEDAT", RepoFieldType.DATE, false, true, false)
        );

        var query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, Collections.emptyList(), Collections.emptyList(), -1);

        assertEquals("select ID,NAME,ISACTIVE,CREATEDAT from TABLE1 limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_single_equal_condition() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields = List.of(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(new RowFilter(new RepoField("ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.EQUALS, 1L));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters, Collections.emptyList(), -1);

        assertEquals("select ID from TABLE1 where (ID=1) limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_single_in_conditionl() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields = List.of(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(new RowFilter(new RepoField("ID", RepoFieldType.LONG, true, false, true), List.of(1L, 2L, 3L)));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters, Collections.emptyList(), -1);

        assertEquals("select ID from TABLE1 where (ID in (1,2,3)) limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_multiple_conditions() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.ORACLE;
        var fields = List.of(
            new RepoField( "ID", RepoFieldType.LONG, true,false, true),
            new RepoField("NAME", RepoFieldType.STRING, false, false, true)
        );
        var filters = List.of(
            new RowFilter(new RepoField("ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.GREATER_OR_EQUAL, 1L),
            new RowFilter(new RepoField("ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.LESS_OR_EQUAL, 1000L),
            new RowFilter(new RepoField("NAME", RepoFieldType.STRING, false, false, false), RowFilterOperator.EQUALS, "MEEP"),
            new RowFilter(new RepoField("ISACTIVE", RepoFieldType.BOOL, false, false, false), RowFilterOperator.EQUALS, true)
        );


        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters, Collections.emptyList(), -1);

        assertEquals("select ID,NAME from TABLE1 where (ID>=1 and ID<=1000 and NAME='MEEP' and ISACTIVE=1) and ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_date_condition_mysql() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields = List.of(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(new RowFilter(new RepoField("CREATEDAT", RepoFieldType.DATE, false, true, false), RowFilterOperator.EQUALS, Date.valueOf(LocalDate.of(1976, 8, 28))));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters, Collections.emptyList(), -1);

        assertEquals("select ID from TABLE1 where (CREATEDAT=str_to_date('1976-08-28','%Y-%m-%d')) limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_date_condition_oracle() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.ORACLE;
        var fields = List.of(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(new RowFilter(new RepoField("CREATEDAT", RepoFieldType.DATE, false, false, false), RowFilterOperator.EQUALS, Date.valueOf(LocalDate.of(1976, 8, 28))));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters, Collections.emptyList(), -1);

        assertEquals("select ID from TABLE1 where (CREATEDAT=to_date('1976-08-28','YYYY-MM-DD')) and ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_like_condition() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.ORACLE;
        var fields = List.of(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(new RowFilter(new RepoField("BEZEICHNUNG", RepoFieldType.STRING, false, false, false), RowFilterOperator.LIKE, "meep%"));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters, Collections.emptyList(), -1);

        assertEquals("select ID from TABLE1 where (upper(BEZEICHNUNG) like 'MEEP%') and ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_escape_quote() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields = List.of(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(new RowFilter(new RepoField("REMARK", RepoFieldType.STRING, false, true, false), RowFilterOperator.EQUALS, "it's old"));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters, Collections.emptyList(), -1);

        assertEquals("select ID from TABLE1 where (REMARK='it''s old') limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_max_results() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields = List.of(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(new RowFilter(new RepoField("CODE", RepoFieldType.STRING, false, false, false), RowFilterOperator.EQUALS, "XXX"));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, filters, Collections.emptyList(), 50);

        assertEquals("select ID from TABLE1 where (CODE='XXX') limit 50", query);
    }


    @Test
    void buildQuery_only_or_conditions() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields = List.of(new RepoField( "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(
            new RowFilter(new RepoField("CODE", RepoFieldType.STRING, false, false, true), RowFilterOperator.EQUALS, "XXX"),
            new RowFilter(new RepoField("NAME", RepoFieldType.STRING, false, false, false), RowFilterOperator.EQUALS, "YYY")
        );

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, Collections.emptyList(), filters, -1);

        assertEquals("select ID from TABLE1 where (CODE='XXX' or NAME='YYY') limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_mixed_and_or_conditions() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.ORACLE;
        var fields = List.of(
            new RepoField( "ID", RepoFieldType.LONG, true,false, true),
            new RepoField("NAME", RepoFieldType.STRING, false, false, true)
        );
        var andFilters = List.of(
            new RowFilter(new RepoField("ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.GREATER_OR_EQUAL, 1L),
            new RowFilter(new RepoField("ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.LESS_OR_EQUAL, 1000L)
        );
        var orFilters = List.of(
            new RowFilter(new RepoField("CODE", RepoFieldType.STRING, false, false, true), RowFilterOperator.EQUALS, "XXX"),
            new RowFilter(new RepoField("NAME", RepoFieldType.STRING, false, false, false), RowFilterOperator.EQUALS, "YYY")
        );


        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", fields, andFilters, orFilters, -1);

        assertEquals("select ID,NAME from TABLE1 where (ID>=1 and ID<=1000) and (CODE='XXX' or NAME='YYY') and ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }
}
