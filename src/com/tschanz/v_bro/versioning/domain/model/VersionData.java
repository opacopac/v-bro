package com.tschanz.v_bro.versioning.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;


public class VersionData {
    public static final LocalDate HIGH_DATE = LocalDate.of(9999, 12, 31);
    public static final LocalDate LOW_DATE = LocalDate.of(2000, 1, 1);

    protected final String id;
    protected final LocalDate gueltigVon;
    protected final LocalDate gueltigBis;
    protected boolean isUnversioned;
    protected final Collection<FwdDependency> fwdDependencies = new ArrayList<>();


    public String getId() { return this.id; }
    public LocalDate getGueltigVon() { return this.gueltigVon; }
    public LocalDate getGueltigBis() { return this.gueltigBis; }
    public boolean isUnversioned() { return this.gueltigVon != null && this.gueltigBis != null; }
    public Collection<FwdDependency> getFwdDependencies() { return this.fwdDependencies; }


    public VersionData(String id) {
        this(id, LOW_DATE, HIGH_DATE);
        this.isUnversioned = true;
    }


    public VersionData(
        String id,
        LocalDate gueltigVon,
        LocalDate gueltigBis
    ) {
        this.id = id;
        this.gueltigVon = gueltigVon;
        this.gueltigBis = gueltigBis;
        this.isUnversioned = false;
    }


    public VersionData(
        String id,
        LocalDate gueltigVon,
        LocalDate gueltigBis,
        Collection<FwdDependency> fwdDependencies
    ) {
        this(id, gueltigVon, gueltigBis);
        this.fwdDependencies.addAll(fwdDependencies);
    }
}
