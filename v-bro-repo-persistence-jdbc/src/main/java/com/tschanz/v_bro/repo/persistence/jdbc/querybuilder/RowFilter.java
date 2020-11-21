package com.tschanz.v_bro.repo.persistence.jdbc.querybuilder;

import com.tschanz.v_bro.repo.persistence.jdbc.model.FieldValue;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class RowFilter {
    @Getter private final RepoField field;
    @Getter private final RowFilterOperator operator;
    @Getter private final List<FieldValue> fieldValues = new ArrayList<>();


    public FieldValue getValue() {
        return this.fieldValues.size() > 0 ? this.fieldValues.get(0) : null;
    }


    public RowFilter(
        RepoField field,
        RowFilterOperator operator,
        Object value
    ) {
        this.field = field;
        this.operator = operator;
        this.fieldValues.add(new FieldValue(field, value));
    }


    public RowFilter(RepoField field, List<Object> values) {
        this.field = field;
        this.operator = RowFilterOperator.IN;
        this.fieldValues.addAll(values
            .stream()
            .map(value -> new FieldValue(field, value))
            .collect(Collectors.toList())
        );
    }
}
