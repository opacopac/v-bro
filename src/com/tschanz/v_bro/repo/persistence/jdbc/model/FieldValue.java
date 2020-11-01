package com.tschanz.v_bro.repo.persistence.jdbc.model;

import java.sql.Date;
import java.sql.Timestamp;
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
        if (this.field.getType() == RepoFieldType.LONG) {
            return (long) this.value;
        } else {
            throw new IllegalArgumentException("field must be of type LONG but is " + this.field.getType().name());
        }
    }


    public boolean getValueBool() {
        if (this.field.getType() == RepoFieldType.BOOL) {
            return (boolean) this.value;
        } else {
            throw new IllegalArgumentException("field must be of type BOOL but is " + this.field.getType().name());
        }
    }


    public LocalDate getValueDate() {
        if (this.field.getType() == RepoFieldType.DATE) {
            return ((Date) this.value).toLocalDate();
        } else if (this.field.getType() == RepoFieldType.TIMESTAMP) {
            return ((Timestamp) this.value).toLocalDateTime().toLocalDate();
        } else {
            throw new IllegalArgumentException("field must be of type DATE or TIMESTAMP but is " + this.field.getType().name());
        }
    }
}
