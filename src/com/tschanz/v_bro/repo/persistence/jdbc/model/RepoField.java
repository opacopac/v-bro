package com.tschanz.v_bro.repo.persistence.jdbc.model;


public class RepoField {
    private final String name;
    private final RepoFieldType type;
    private final boolean isId;
    private final boolean isNullable;
    private final boolean isUnique;

    public String getName() { return this.name; }
    public RepoFieldType getType() {
        return this.type;
    }
    public boolean getIsId() { return this.isId; }
    public boolean getIsNullable() { return this.isNullable; }
    public boolean getIsUnique() { return this.isUnique; }


    public RepoField(
        String name,
        RepoFieldType type,
        boolean isId,
        boolean isNullable,
        boolean isUnique
    ) {
        this.name = name;
        this.type = type;
        this.isId = isId;
        this.isNullable = isNullable;
        this.isUnique = isUnique;
    }
}
