package com.tschanz.v_bro.repo.persistence.jdbc.model;


public class RepoField {
    public String tableName; // TODO
    private final String name;
    private final RepoFieldType type;
    private final boolean _isId;
    private final boolean _isNullable;
    private final boolean _isUnique;

    public String getName() { return this.name; }
    public RepoFieldType getType() {
        return this.type;
    }
    public boolean isId() { return this._isId; }
    public boolean isNullable() { return this._isNullable; }
    public boolean isUnique() { return this._isUnique; }


    public RepoField(
        String name,
        RepoFieldType type,
        boolean isId,
        boolean isNullable,
        boolean isUnique
    ) {
        this.name = name.toUpperCase();
        this.type = type;
        this._isId = isId;
        this._isNullable = isNullable;
        this._isUnique = isUnique;
    }
}
