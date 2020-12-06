package com.tschanz.v_bro.repo.persistence.jdbc.querybuilder;

import com.tschanz.v_bro.repo.persistence.jdbc.model.FieldValue;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactory;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcServerType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class JdbcQueryBuilderImpl implements JdbcQueryBuilder {
    public static int MAX_ROW_NUM_HARD_LIMIT = 10000;
    private final JdbcConnectionFactory connectionFactory;


    @Override
    public String buildQuery(
        @NonNull String tableName,
        @NonNull List<RepoField> selectFields,
        @NonNull List<RowFilter> andFilters,
        @NonNull List<RowFilter> orFilters,
        int maxResults
    ) {
        var resultLimit = maxResults > 0 ? Math.min(maxResults, MAX_ROW_NUM_HARD_LIMIT) : MAX_ROW_NUM_HARD_LIMIT;
        return "select " + this.getSelectFields(selectFields)
            + " from " + tableName
            + this.getWhereClause(andFilters, orFilters, resultLimit)
            + this.getLimitClause(resultLimit);
    }

    private String getSelectFields(List<RepoField> fields) {
        return fields
            .stream()
            .map(RepoField::getName)
            .collect(Collectors.joining(","));
    }


    private String getWhereCondition(RowFilter rowFilter) {
        var column = rowFilter.getField().getName();
        if (rowFilter.getOperator() == RowFilterOperator.LIKE) {
            column = "upper(" + column + ")";
        }

        return column + this.getOperatorAndValue(rowFilter);
    }


    private String getOperatorAndValue(RowFilter rowFilter) {
        switch (rowFilter.getOperator()) {
            case LESS_OR_EQUAL:
                return "<=" + this.getFilterValue(rowFilter.getValue());
            case GREATER_OR_EQUAL:
                return ">=" + this.getFilterValue(rowFilter.getValue());
            case IN:
                return " in (" + rowFilter.getFieldValues().stream().map(this::getFilterValue).collect(Collectors.joining(",")) + ")";
            case LIKE:
                return " like " + this.getFilterValue(rowFilter.getValue()).toUpperCase();
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


    private String getWhereClause(List<RowFilter> andFilters, List<RowFilter> orFilters, int resultLimit) {
        var andCondition = this.getAndOrConditions(andFilters, true);
        var orCondition = this.getAndOrConditions(orFilters, false);
        var limitCondition = this.getLimitCondition(resultLimit);
        var conditionSql = List.of(andCondition, orCondition, limitCondition)
            .stream()
            .filter(c -> !c.isEmpty())
            .collect(Collectors.joining(" and "));

        return !conditionSql.isEmpty() ? " where " + conditionSql : "";
    }


    private String getAndOrConditions(List<RowFilter> andFilters, boolean isAnd) {
        var andOrSql = isAnd ? " and " : " or ";
        if (andFilters != null && andFilters.size() > 0) {
            return "(" + andFilters
                .stream()
                .map(this::getWhereCondition)
                .collect(Collectors.joining(andOrSql)) + ")";
        } else {
            return "";
        }
    }


    private String getLimitCondition(int resultLimit) {
        if (this.connectionFactory.getJdbcServerType() == JdbcServerType.ORACLE) {
            return "ROWNUM<=" + resultLimit;
        } else {
            return "";
        }
    }


    private String getLimitClause(int resultLimit) {
        if (this.connectionFactory.getJdbcServerType() == JdbcServerType.MYSQL) {
            return " limit " + resultLimit;
        } else {
            return "";
        }
    }
}
