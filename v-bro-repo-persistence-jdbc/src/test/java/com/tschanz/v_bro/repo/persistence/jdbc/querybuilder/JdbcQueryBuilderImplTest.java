package com.tschanz.v_bro.repo.persistence.jdbc.querybuilder;

import com.tschanz.v_bro.repo.persistence.jdbc.mock.SpyConnectionFactory;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoFieldType;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableJoin;
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
        var fields = List.of(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true,false, true));

        var query = this.jdbcQueryBuilder.buildQuery("TABLE1", Collections.emptyList(), fields, Collections.emptyList(), Collections.emptyList(), -1);

        assertEquals("select TABLE1.ID from TABLE1 where ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_multiple_fields_no_condition_mysql() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields = List.of(
            new RepoField("TABLE1", "ID", RepoFieldType.LONG, true,false, true),
            new RepoField("TABLE1", "NAME", RepoFieldType.STRING, false, false, true),
            new RepoField("TABLE1", "ISACTIVE", RepoFieldType.BOOL, false, false, false),
            new RepoField("TABLE1", "CREATEDAT", RepoFieldType.DATE, false, true, false)
        );

        var query = this.jdbcQueryBuilder.buildQuery("TABLE1", Collections.emptyList(), fields, Collections.emptyList(), Collections.emptyList(), -1);

        assertEquals("select TABLE1.ID,TABLE1.NAME,TABLE1.ISACTIVE,TABLE1.CREATEDAT from TABLE1 limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_single_equal_condition() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields = List.of(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(new RowFilter(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.EQUALS, 1L));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", Collections.emptyList(), fields, filters, Collections.emptyList(), -1);

        assertEquals("select TABLE1.ID from TABLE1 where (TABLE1.ID=1) limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_single_in_conditionl() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields = List.of(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(new RowFilter(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true, false, true), List.of(1L, 2L, 3L)));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", Collections.emptyList(), fields, filters, Collections.emptyList(), -1);

        assertEquals("select TABLE1.ID from TABLE1 where (TABLE1.ID in (1,2,3)) limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_multiple_conditions() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.ORACLE;
        var fields = List.of(
            new RepoField("TABLE1", "ID", RepoFieldType.LONG, true,false, true),
            new RepoField("TABLE1", "NAME", RepoFieldType.STRING, false, false, true)
        );
        var filters = List.of(
            new RowFilter(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.GREATER_OR_EQUAL, 1L),
            new RowFilter(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.LESS_OR_EQUAL, 1000L),
            new RowFilter(new RepoField("TABLE1", "NAME", RepoFieldType.STRING, false, false, false), RowFilterOperator.EQUALS, "MEEP"),
            new RowFilter(new RepoField("TABLE1", "ISACTIVE", RepoFieldType.BOOL, false, false, false), RowFilterOperator.EQUALS, true)
        );


        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", Collections.emptyList(), fields, filters, Collections.emptyList(), -1);

        assertEquals("select TABLE1.ID,TABLE1.NAME from TABLE1 where (TABLE1.ID>=1 and TABLE1.ID<=1000 and TABLE1.NAME='MEEP' and TABLE1.ISACTIVE=1) and ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_date_condition_mysql() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields = List.of(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(new RowFilter(new RepoField("TABLE1", "CREATEDAT", RepoFieldType.DATE, false, true, false), RowFilterOperator.EQUALS, Date.valueOf(LocalDate.of(1976, 8, 28))));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", Collections.emptyList(), fields, filters, Collections.emptyList(), -1);

        assertEquals("select TABLE1.ID from TABLE1 where (TABLE1.CREATEDAT=str_to_date('1976-08-28','%Y-%m-%d')) limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_date_condition_oracle() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.ORACLE;
        var fields = List.of(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(new RowFilter(new RepoField("TABLE1", "CREATEDAT", RepoFieldType.DATE, false, false, false), RowFilterOperator.EQUALS, Date.valueOf(LocalDate.of(1976, 8, 28))));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", Collections.emptyList(), fields, filters, Collections.emptyList(), -1);

        assertEquals("select TABLE1.ID from TABLE1 where (TABLE1.CREATEDAT=to_date('1976-08-28','YYYY-MM-DD')) and ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_like_condition() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.ORACLE;
        var fields = List.of(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(new RowFilter(new RepoField("TABLE1", "BEZEICHNUNG", RepoFieldType.STRING, false, false, false), RowFilterOperator.LIKE, "meep%"));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", Collections.emptyList(), fields, filters, Collections.emptyList(), -1);

        assertEquals("select TABLE1.ID from TABLE1 where (upper(TABLE1.BEZEICHNUNG) like 'MEEP%') and ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_escape_quote() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields = List.of(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(new RowFilter(new RepoField("TABLE1", "REMARK", RepoFieldType.STRING, false, true, false), RowFilterOperator.EQUALS, "it's old"));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", Collections.emptyList(), fields, filters, Collections.emptyList(), -1);

        assertEquals("select TABLE1.ID from TABLE1 where (TABLE1.REMARK='it''s old') limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_max_results() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields = List.of(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(new RowFilter(new RepoField("TABLE1", "CODE", RepoFieldType.STRING, false, false, false), RowFilterOperator.EQUALS, "XXX"));

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", Collections.emptyList(), fields, filters, Collections.emptyList(), 50);

        assertEquals("select TABLE1.ID from TABLE1 where (TABLE1.CODE='XXX') limit 50", query);
    }


    @Test
    void buildQuery_only_or_conditions() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields = List.of(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true,false, true));
        var filters = List.of(
            new RowFilter(new RepoField("TABLE1", "CODE", RepoFieldType.STRING, false, false, true), RowFilterOperator.EQUALS, "XXX"),
            new RowFilter(new RepoField("TABLE1", "NAME", RepoFieldType.STRING, false, false, false), RowFilterOperator.EQUALS, "YYY")
        );

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", Collections.emptyList(), fields, Collections.emptyList(), filters, -1);

        assertEquals("select TABLE1.ID from TABLE1 where (TABLE1.CODE='XXX' or TABLE1.NAME='YYY') limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_mixed_and_or_conditions() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.ORACLE;
        var fields = List.of(
            new RepoField("TABLE1", "ID", RepoFieldType.LONG, true,false, true),
            new RepoField("TABLE1", "NAME", RepoFieldType.STRING, false, false, true)
        );
        var andFilters = List.of(
            new RowFilter(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.GREATER_OR_EQUAL, 1L),
            new RowFilter(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.LESS_OR_EQUAL, 1000L)
        );
        var orFilters = List.of(
            new RowFilter(new RepoField("TABLE1", "CODE", RepoFieldType.STRING, false, false, true), RowFilterOperator.EQUALS, "XXX"),
            new RowFilter(new RepoField("TABLE1", "NAME", RepoFieldType.STRING, false, false, false), RowFilterOperator.EQUALS, "YYY")
        );


        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", Collections.emptyList(), fields, andFilters, orFilters, -1);

        assertEquals("select TABLE1.ID,TABLE1.NAME from TABLE1 where (TABLE1.ID>=1 and TABLE1.ID<=1000) and (TABLE1.CODE='XXX' or TABLE1.NAME='YYY') and ROWNUM<=" + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }


    @Test
    void buildQuery_join_table() {
        this.spyConnectionFactory.jdbcServerType = JdbcServerType.MYSQL;
        var fields1 = List.of(
            new RepoField("TABLE1", "ID", RepoFieldType.LONG, true,false, true),
            new RepoField("TABLE1", "NAME", RepoFieldType.STRING, false, false, true)
        );
        var table1 = new RepoTable("TABLE1", fields1, Collections.emptyList(), Collections.emptyList());
        var fields2 = List.of(
            new RepoField("TABLE2", "ID", RepoFieldType.LONG, true,false, true),
            new RepoField("TABLE2", "VORNAME", RepoFieldType.STRING, false, false, true)
        );
        var table2 = new RepoTable("TABLE2", fields2, Collections.emptyList(), Collections.emptyList());
        var joins = List.of(new RepoTableJoin(table1, table2, fields1.get(1), fields2.get(1)));
        var orFilters = List.of(
            new RowFilter(new RepoField("TABLE1", "ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.EQUALS, 33L),
            new RowFilter(new RepoField("TABLE2", "ID", RepoFieldType.LONG, true, false, true), RowFilterOperator.EQUALS, 44L)
        );

        String query = this.jdbcQueryBuilder.buildQuery("TABLE1", joins, List.of(fields1.get(0), fields2.get(1)), Collections.emptyList(), orFilters, -1);

        assertEquals("select TABLE1.ID,TABLE2.VORNAME from TABLE1 left join TABLE2 on TABLE1.NAME=TABLE2.VORNAME where (TABLE1.ID=33 or TABLE2.ID=44) limit " + JdbcQueryBuilderImpl.MAX_ROW_NUM_HARD_LIMIT, query);
    }
}
