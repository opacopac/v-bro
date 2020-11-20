package com.tschanz.v_bro.repo.persistence.jdbc.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class RepoTable {
    @Getter protected final String name;
    @Getter protected final List<RepoField> fields;
    @Getter protected final List<RepoRelation> outgoingRelations;
    @Getter protected final List<RepoRelation> incomingRelations;


    public RepoField findField(String fieldName) {
        return this.fields
            .stream()
            .filter(field -> field.getName().toUpperCase().equals(fieldName.toUpperCase()))
            .findFirst()
            .orElse(null);
    }


    public List<RepoField> findAllFields(String... fieldNames) {
        return Arrays.stream(fieldNames)
            .map(this::findField)
            .collect(Collectors.toList());
    }


    public RepoField findfirstIdField() {
        return this.findAllIdFields()
            .stream()
            .findFirst()
            .orElse(null);
    }


    public List<RepoField> findAllIdFields() {
        return this.fields
            .stream()
            .filter(RepoField::isId)
            .collect(Collectors.toList());
    }


    public List<RepoField> findUniqueFields() {
        return this.fields
            .stream()
            .filter(RepoField::isUnique)
            .collect(Collectors.toList());
    }
}
