package com.tschanz.v_bro.structure.persistence.jdbc.model;

import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoFieldType;

import java.util.Comparator;


public class DenominationFieldComparator implements Comparator<RepoField> {
    @Override
    public int compare(RepoField field1, RepoField field2) {
        if (field1.isUnique() != field2.isUnique()) {
            return field1.isUnique() ? -1 : 1; // unique before non-unique
        } else if (field1.isNullable() != field2.isNullable()) {
            return field2.isNullable() ? -1 : 1; // non-null before null
        } else if (field1.getType() !=  field2.getType()) {
            return this.getTypeValue(field1.getType()) - this.getTypeValue(field2.getType()); // field type string before long before bool before date before timestamp
        } else {
            return 0;
        }
    }


    private int getTypeValue(RepoFieldType type) {
        switch (type) {
            case STRING: return 1;
            case LONG: return 2;
            case BOOL: return 3;
            case DATE: return 4;
            case TIMESTAMP:
            default: return 5;
        }
    }
}