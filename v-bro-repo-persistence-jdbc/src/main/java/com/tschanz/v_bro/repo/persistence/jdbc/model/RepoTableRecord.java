package com.tschanz.v_bro.repo.persistence.jdbc.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class RepoTableRecord {
    @NonNull private final RepoTable repoTable;
    @NonNull private final List<FieldValue> fieldValues;


    public static RepoTableRecord fromResultSet(RepoTable repoTable, List<RepoField> fields, ResultSet resultSet) throws SQLException {
        List<FieldValue> fieldValues = new ArrayList<>();

        for (var field: fields) {
            var fieldValue = FieldValue.fromResultSet(field, resultSet);
            fieldValues.add(fieldValue);
        }

        return new RepoTableRecord(repoTable, fieldValues);
    }


    public FieldValue findFieldValue(String fieldName) {
        return this.findFieldValue(this.repoTable.getName(), fieldName);
    }


    public FieldValue findFieldValue(String tableName, String fieldName) {
        return this.fieldValues
            .stream()
            .filter(fieldValue -> fieldValue.getField().getTableName().toUpperCase().equals(tableName.toUpperCase())
                && fieldValue.getName().toUpperCase().equals(fieldName.toUpperCase()))
            .findFirst()
            .orElse(null);
    }


    public FieldValue findIdFieldValue() {
        String idFieldName = this.repoTable.findfirstIdField().getName();
        if (idFieldName == null) {
            return null;
        } else {
            return this.findFieldValue(idFieldName);
        }
    }
}
