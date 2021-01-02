package com.tschanz.v_bro.repo.persistence.jdbc.querybuilder;

import com.tschanz.v_bro.repo.persistence.jdbc.model.FieldValue;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableJoin;
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
        @NonNull List<RepoTableJoin> tableJoins,
        @NonNull List<RepoField> selectFields,
        @NonNull List<RowFilter> mandatoryFilters,
        @NonNull List<RowFilter> optFilters,
        int maxResults
    ) {
        var resultLimit = maxResults > 0 ? Math.min(maxResults, MAX_ROW_NUM_HARD_LIMIT) : MAX_ROW_NUM_HARD_LIMIT;
        return this.buildSelectClause(selectFields)
            + this.buildFromClause(tableName)
            + this.buildJoinClause(tableJoins)
            + this.buildWhereClause(mandatoryFilters, optFilters, resultLimit)
            + this.buildLimitClause(resultLimit);
    }

    private String buildSelectClause(List<RepoField> fields) {
        return "select " + fields
            .stream()
            .map(f -> String.format("%s.%s as %s__%s", f.getTableName(), f.getName(), f.getTableName(), f.getName()))
            .collect(Collectors.joining(","));
    }


    private String buildFromClause(String tableName) {
        return " from " + tableName;
    }


    private String buildJoinClause(List<RepoTableJoin> joins) {
        return joins
            .stream()
            .map(j -> String.format(" left join %s on %s.%s=%s.%s",
                j.getSecondaryTable().getName(),
                j.getPrimaryTable().getName(),
                j.getPrimaryTableField().getName(),
                j.getSecondaryTable().getName(),
                j.getSecondaryTableField().getName()
            ))
            .collect(Collectors.joining());
    }


    private String buildWhereClause(List<RowFilter> mandatoryFilters, List<RowFilter> optFilters, int resultLimit) {
        var andCondition = this.buildAndOrConditions(mandatoryFilters, true);
        var orCondition = this.buildAndOrConditions(optFilters, false);
        var limitCondition = this.buildLimitCondition(resultLimit);
        var conditionSql = List.of(andCondition, orCondition, limitCondition)
            .stream()
            .filter(c -> !c.isEmpty())
            .collect(Collectors.joining(" and "));

        return !conditionSql.isEmpty() ? " where " + conditionSql : "";
    }


    private String buildAndOrConditions(List<RowFilter> andFilters, boolean isAnd) {
        var andOrSql = isAnd ? " and " : " or ";
        if (andFilters != null && andFilters.size() > 0) {
            return "(" + andFilters
                .stream()
                .map(this::buildWhereCondition)
                .collect(Collectors.joining(andOrSql)) + ")";
        } else {
            return "";
        }
    }


    private String buildWhereCondition(RowFilter rowFilter) {
        var column = String.format("%s.%s", rowFilter.getField().getTableName(), rowFilter.getField().getName());
        if (rowFilter.getOperator() == RowFilterOperator.LIKE) {
            column = "upper(" + column + ")";
        }

        return column + this.buildOperatorAndValue(rowFilter);
    }


    private String buildOperatorAndValue(RowFilter rowFilter) {
        switch (rowFilter.getOperator()) {
            case LESS_OR_EQUAL:
                return "<=" + this.buildFilterValue(rowFilter.getValue());
            case GREATER_OR_EQUAL:
                return ">=" + this.buildFilterValue(rowFilter.getValue());
            case IN:
                return " in (" + rowFilter.getFieldValues().stream().map(this::buildFilterValue).collect(Collectors.joining(",")) + ")";
            case LIKE:
                return " like " + this.buildFilterValue(rowFilter.getValue()).toUpperCase();
            case EQUALS:
            default:
                return "=" + this.buildFilterValue(rowFilter.getValue());
        }
    }


    private String buildFilterValue(FieldValue filterValue) {
        switch (filterValue.getType()) {
            case BOOL:
                return filterValue.getValueBool() ? "1" : "0";
            case DATE:
                return this.buildDateFilterValue(filterValue.getValueDate());
            case LONG:
                return String.valueOf(filterValue.getValueLong());
            case STRING:
            default:
                return "'" + this.escapeString(String.valueOf(filterValue.getValueString())) + "'";
        }
    }


    private String buildDateFilterValue(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        switch (this.connectionFactory.getJdbcServerType()) {
            case MYSQL:
                return "str_to_date('" + formatter.format(date) + "','%Y-%m-%d')";
            case ORACLE:
            default:
                return "to_date('" + formatter.format(date) + "','YYYY-MM-DD')";
        }
    }


    private String escapeString(String value) {
        return value.replace("'", "''");
    }


    private String buildLimitCondition(int resultLimit) {
        if (this.connectionFactory.getJdbcServerType() == JdbcServerType.ORACLE) {
            return "ROWNUM<=" + resultLimit;
        } else {
            return "";
        }
    }


    private String buildLimitClause(int resultLimit) {
        if (this.connectionFactory.getJdbcServerType() == JdbcServerType.MYSQL) {
            return " limit " + resultLimit;
        } else {
            return "";
        }
    }
}
