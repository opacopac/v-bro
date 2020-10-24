package com.tschanz.v_bro.versioning.xml.model;

import com.tschanz.v_bro.versioning.domain.model.VersionInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;


public class XmlVersionInfo extends VersionInfo {
    private final Collection<String> fwdDepIds = new ArrayList<>();


    public Collection<String> getFwdDepIds() { return fwdDepIds; }


    public XmlVersionInfo(String id) {
        super(id);
    }


    public XmlVersionInfo(String id, LocalDate gueltigVon, LocalDate gueltigBis) {
        super(id, gueltigVon, gueltigBis, VersionInfo.DEFAULT_PFLEGESTATUS);
    }


    public void addFwdDepIds(Collection<String> fwdDepIds) {
        this.fwdDepIds.addAll(fwdDepIds);
    }
}
