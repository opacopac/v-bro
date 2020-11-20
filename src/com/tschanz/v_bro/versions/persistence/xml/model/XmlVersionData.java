package com.tschanz.v_bro.versions.persistence.xml.model;

import com.tschanz.v_bro.versions.domain.model.VersionData;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;


public class XmlVersionData extends VersionData {
    @Getter private final Collection<String> fwdDepIds = new ArrayList<>();


    public XmlVersionData(String id) {
        super(id);
    }


    public XmlVersionData(String id, LocalDate gueltigVon, LocalDate gueltigBis) {
        super(id, gueltigVon, gueltigBis, VersionData.DEFAULT_PFLEGESTATUS);
    }


    public void addFwdDepIds(Collection<String> fwdDepIds) {
        this.fwdDepIds.addAll(fwdDepIds);
    }
}
