package com.tschanz.v_bro.repo.jdbc.querybuilder;

import com.tschanz.v_bro.repo.jdbc.model.FieldValue;
import com.tschanz.v_bro.repo.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.jdbc.service.JdbcConnectionFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class JdbcQueryBuilderImpl implements JdbcQueryBuilder {
    public static int MAX_ROW_NUM = 10000;
    private final JdbcConnectionFactory connectionFactory;


    public JdbcQueryBuilderImpl(JdbcConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    @Override
    public String createFieldName(RepoField field) {
        return field.getName();
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
            .map(this::createFieldName)
            .collect(Collectors.joining(","));
    }


    private String getWhereCondition(RowFilter rowFilter) {
        return this.createFieldName(rowFilter.getFieldValue().getField())
            + this.getOperator(rowFilter.getOperator())
            + this.getFilterValue(rowFilter.getFieldValue());
    }


    private String getOperator(RowFilterOperator operator) {
        switch (operator) {
            case LESS_OR_EQUAL:
                return "<=";
            case GREATER_OR_EQUAL:
                return ">=";
            case EQUALS:
            default:
                return "=";
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


    private String getDateFilterValue(Date date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (this.connectionFactory.isCurrentConnectionMySql()) {
            return "str_to_date('" + formatter.format(date) + "','%Y-%m-%d')";
        } else {
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

        if (!this.connectionFactory.isCurrentConnectionMySql()) {
            whereClause += whereClause.isEmpty() ? " where " : " and ";
            whereClause += "ROWNUM<=" + MAX_ROW_NUM;
        }

        return whereClause;
    }


    private String getLimitClause() {
        if (!this.connectionFactory.isCurrentConnectionMySql()) {
            return "";
        }

        return " limit " + MAX_ROW_NUM;
    }
}
