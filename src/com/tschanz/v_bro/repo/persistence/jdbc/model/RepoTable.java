package com.tschanz.v_bro.repo.persistence.jdbc.model;

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
            .orElseThrow(() -> new IllegalArgumentException("no field found with name " + fieldName));
    }


    public List<RepoField> findIdFields() {
        return this.fields
            .stream()
            .filter(RepoField::getIsId)
            .collect(Collectors.toList());
    }


    public List<RepoField> findUniqueFields() {
        return this.fields
            .stream()
            .filter(RepoField::getIsUnique)
            .collect(Collectors.toList());
    }
}
