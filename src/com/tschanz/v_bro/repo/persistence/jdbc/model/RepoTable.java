package com.tschanz.v_bro.repo.persistence.jdbc.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class RepoTable {
    protected final String name;
    protected final List<RepoField> fields;
    protected final List<RepoRelation> outgoingRelations;
    protected final List<RepoRelation> incomingRelations;


    public String getName() { return name; }
    public List<RepoField> getFields() { return fields; }
    public List<RepoRelation> getOutgoingRelations() { return outgoingRelations; }
    public List<RepoRelation> getIncomingRelations() { return incomingRelations; }


    public RepoTable(
        String name,
        List<RepoField> fields,
        List<RepoRelation> outgoingRelations,
        List<RepoRelation> incomingRelations
    ) {
        this.name = name;
        this.fields = fields;
        this.outgoingRelations = outgoingRelations;
        this.incomingRelations = incomingRelations;
    }


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
