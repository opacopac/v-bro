package com.tschanz.v_bro.repo.persistence.jdbc.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;


@Getter
@RequiredArgsConstructor
public class FieldValue {
    @NonNull private final RepoField field;
    private final Object value;


    public static FieldValue fromResultSet(RepoField field, ResultSet resultSet) throws SQLException {
        var fieldName = String.format("%s__%s", field.getTableName(), field.getName());
        switch (field.getType()) {
            case BOOL:
                var boolResult = resultSet.getBoolean(fieldName);
                return new FieldValue(field, resultSet.wasNull() ? null : boolResult);
            case LONG:
                var longResult = resultSet.getLong(fieldName);
                return new FieldValue(field, resultSet.wasNull() ? null : longResult);
            case DATE:
                var dateResult = resultSet.getDate(fieldName);
                return new FieldValue(field, resultSet.wasNull() ? null : dateResult);
            case TIMESTAMP:
                var timestampResult = resultSet.getTimestamp(fieldName);
                return new FieldValue(field, resultSet.wasNull() ? null : timestampResult);
            case STRING:
            default:
                var stringResult = resultSet.getString(fieldName);
                return new FieldValue(field, resultSet.wasNull() ? null : stringResult);
        }
    }


    public String getName() {
        return this.field.getName();
    }


    public RepoFieldType getType() {
        return this.field.getType();
    }


    public String getValueString() {
        if (this.value == null) {
            return null;
        } else {
            return String.valueOf(this.value);
        }
    }


    public Long getValueLong() {
        if (this.value == null) {
            return null;
        } else if (this.field.getType() == RepoFieldType.LONG) {
            return (long) this.value;
        } else {
            throw new IllegalArgumentException("field must be of type LONG but is " + this.field.getType().name());
        }
    }


    public Boolean getValueBool() {
        if (this.value == null) {
            return null;
        } else if (this.field.getType() == RepoFieldType.BOOL) {
            return (boolean) this.value;
        } else {
            throw new IllegalArgumentException("field must be of type BOOL but is " + this.field.getType().name());
        }
    }


    public LocalDate getValueDate() {
        if (this.value == null) {
            return null;
        } else if (this.field.getType() == RepoFieldType.DATE) {
            return ((Date) this.value).toLocalDate();
        } else if (this.field.getType() == RepoFieldType.TIMESTAMP) {
            return ((Timestamp) this.value).toLocalDateTime().toLocalDate();
        } else {
            throw new IllegalArgumentException("field must be of type DATE or TIMESTAMP but is " + this.field.getType().name());
        }
    }
}
