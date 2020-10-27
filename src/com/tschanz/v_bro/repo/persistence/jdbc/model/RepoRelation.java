package com.tschanz.v_bro.repo.persistence.jdbc.model;


public class RepoRelation {
    private final String bwdClassName;
    private final String bwdFieldName;
    private final String fwdClassName;
    private final String fwdFieldName;


    public String getBwdClassName() { return this.bwdClassName; }
    public String getBwdFieldName() { return this.bwdFieldName; }
    public String getFwdClassName() { return this.fwdClassName; }
    public String getFwdFieldName() { return this.fwdFieldName; }


    public RepoRelation(String bwdClassName, String bwdFieldName, String fwdClassName, String fwdFieldName) {
        this.bwdClassName = bwdClassName;
        this.bwdFieldName = bwdFieldName;
        this.fwdClassName = fwdClassName;
        this.fwdFieldName = fwdFieldName;
    }
}
