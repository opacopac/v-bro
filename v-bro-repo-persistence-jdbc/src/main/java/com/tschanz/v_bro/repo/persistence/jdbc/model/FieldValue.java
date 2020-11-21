package com.tschanz.v_bro.repo.persistence.jdbc.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;


@RequiredArgsConstructor
public class FieldValue {
    @Getter private final RepoField field;
    @Getter private final Object value;


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
