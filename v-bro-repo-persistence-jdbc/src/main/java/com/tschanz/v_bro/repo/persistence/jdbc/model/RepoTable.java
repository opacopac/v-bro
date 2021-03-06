package com.tschanz.v_bro.repo.persistence.jdbc.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class RepoTable {
    @NonNull private final String name;
    @NonNull private final List<RepoField> fields;
    @NonNull private final List<RepoRelation> outgoingRelations;
    @NonNull private final List<RepoRelation> incomingRelations;


    public RepoField findField(String fieldName) {
        return this.fields
            .stream()
            .filter(field -> field.getName().toUpperCase().equals(fieldName.toUpperCase()))
            .findFirst()
            .orElse(null);
    }


    public RepoField findfirstPkField() {
        return this.findAllPkFields()
            .stream()
            .findFirst()
            .orElse(null);
    }


    public List<RepoField> findAllPkFields() {
        return this.fields
            .stream()
            .filter(RepoField::isPk)
            .collect(Collectors.toList());
    }
}
