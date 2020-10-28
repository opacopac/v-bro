package com.tschanz.v_bro.repo.persistence.jdbc.model;

import java.sql.Date;
import java.time.LocalDate;


public class FieldValue {
    private final RepoField field;
    private final Object value;


    public RepoField getField() { return field; }
    public String getName() { return this.field.getName(); }
    public RepoFieldType getType() {
        return this.field.getType();
    }
    public Object getValue() { return this.value; }


    public FieldValue(RepoField field, Object value) {
        this.field = field;
        this.value = value;
    }


    public String getValueString() {
        return String.valueOf(this.value);
    }


    public long getValueLong() {
        this.checkTypeOrThrow(RepoFieldType.LONG);
        return (long) this.value;
    }


    public boolean getValueBool() {
        this.checkTypeOrThrow(RepoFieldType.BOOL);
        return (boolean) this.value;
    }


    public LocalDate getValueDate() {
        this.checkTypeOrThrow(RepoFieldType.DATE);
        return ((Date) this.value).toLocalDate();
    }


    private void checkTypeOrThrow(RepoFieldType type) {
        if (type != this.field.getType()) {
            throw new IllegalArgumentException("field must be of type " + type.name());
        }
    }
}
