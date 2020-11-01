package com.tschanz.v_bro.versions.persistence.xml.service;

import com.tschanz.v_bro.element_classes.persistence.xml.model.XmlElementLutInfo;
import com.tschanz.v_bro.element_classes.persistence.xml.service.XmlElementClassService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;
import com.tschanz.v_bro.versions.domain.service.VersionService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class XmlVersionService implements VersionService {
    private final XmlRepoService repoService;
    private final VersionParser parser;


    public XmlVersionService(
        XmlRepoService repoService,
        VersionParser parser
    ) {
        this.repoService = repoService;
        this.parser = parser;
    }


    @Override
    public List<VersionInfo> readVersionTimeline(String elementClass, String elementId) throws RepoException {
        XmlElementLutInfo elementLut = this.repoService.getElementLut().get(elementId);
        if (elementLut == null) {
            throw new IllegalArgumentException("element id not found");
        }

        InputStream xmlFileStream = this.repoService.getNewXmlFileStream(elementLut.getStartBytePos(), elementLut.getEndBytePos());
        Collection<VersionInfo> versions = this.parser.readVersions(
            xmlFileStream,
            elementClass,
            elementId
        );

        return new ArrayList<>(versions);
    }
}
