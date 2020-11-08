package com.tschanz.v_bro.repo.persistence.jdbc.querybuilder;

import com.tschanz.v_bro.repo.persistence.jdbc.model.FieldValue;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactory;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcServerType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


public class JdbcQueryBuilderImpl implements JdbcQueryBuilder {
    public static int MAX_ROW_NUM = 10000;
    private final JdbcConnectionFactory connectionFactory;


    public JdbcQueryBuilderImpl(JdbcConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    @Override
    public String buildQuery(String tableName, List<RepoField> selectFields, List<RowFilter> rowFilters) {
        return "select " + this.getSelectFields(selectFields)
            + " from " + tableName
            + this.getWhereClause(rowFilters)
            + this.getLimitClause();
    }

    private String getSelectFields(List<RepoField> fields) {
        return fields
            .stream()
            .map(RepoField::getName)
            .collect(Collectors.joining(","));
    }


    private String getWhereCondition(RowFilter rowFilter) {
        return rowFilter.getField().getName() + this.getOperatorAndValue(rowFilter);
    }


    private String getOperatorAndValue(RowFilter rowFilter) {
        switch (rowFilter.getOperator()) {
            case LESS_OR_EQUAL:
                return "<=" + this.getFilterValue(rowFilter.getValue());
            case GREATER_OR_EQUAL:
                return ">=" + this.getFilterValue(rowFilter.getValue());
            case IN:
                return " IN (" + rowFilter.getValues().stream().map(this::getFilterValue).collect(Collectors.joining(",")) + ")";
            case EQUALS:
            default:
                return "=" + this.getFilterValue(rowFilter.getValue());
        }
    }


    private String getFilterValue(FieldValue filterValue) {
        switch (filterValue.getType()) {
            case BOOL:
                return filterValue.getValueBool() ? "1" : "0";
            case DATE:
                return this.getDateFilterValue(filterValue.getValueDate());
            case LONG:
                return String.valueOf(filterValue.getValueLong());
            case STRING:
            default:
                return "'" + this.escapeString(String.valueOf(filterValue.getValueString())) + "'";
        }
    }


    private String escapeString(String value) {
        return value.replace("'", "''");
    }


    private String getDateFilterValue(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        switch (this.connectionFactory.getJdbcServerType()) {
            case MYSQL:
                return "str_to_date('" + formatter.format(date) + "','%Y-%m-%d')";
            case ORACLE:
            default:
                return "to_date('" + formatter.format(date) + "','YYYY-MM-DD')";
        }
    }


    private String getWhereClause(List<RowFilter> rowFilters) {
        String whereClause = "";
        if (rowFilters != null && rowFilters.size() > 0) {
            whereClause += " where " + rowFilters
                .stream()
                .map(this::getWhereCondition)
                .collect(Collectors.joining(" and "));
        }

        if (this.connectionFactory.getJdbcServerType() == JdbcServerType.ORACLE) {
            whereClause += whereClause.isEmpty() ? " where " : " and ";
            whereClause += "ROWNUM<=" + MAX_ROW_NUM;
        }

        return whereClause;
    }


    private String getLimitClause() {
        if (this.connectionFactory.getJdbcServerType() == JdbcServerType.MYSQL) {
            return " limit " + MAX_ROW_NUM;
        } else {
            return "";
        }
    }
}
