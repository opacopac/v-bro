package com.tschanz.v_bro.versioning.xml.model;

import com.tschanz.v_bro.versioning.domain.model.FwdDependency;
import com.tschanz.v_bro.versioning.domain.model.VersionData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;


public class XmlVersionData extends VersionData {
    private final Collection<String> fwdDepIds = new ArrayList<>();


    public Collection<String> getFwdDepIds() { return fwdDepIds; }


    public XmlVersionData(String id) {
        super(id);
    }


    public XmlVersionData(String id, LocalDate gueltigVon, LocalDate gueltigBis) {
        super(id, gueltigVon, gueltigBis);
    }


    public XmlVersionData(String id, LocalDate gueltigVon, LocalDate gueltigBis, Collection<FwdDependency> fwdDependencies) {
        super(id, gueltigVon, gueltigBis, fwdDependencies);
    }


    public void addFwdDepIds(Collection<String> fwdDepIds) {
        this.fwdDepIds.addAll(fwdDepIds);
    }
}
