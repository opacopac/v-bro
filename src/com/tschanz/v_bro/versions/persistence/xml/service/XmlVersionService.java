package com.tschanz.v_bro.versions.persistence.xml.service;

import com.tschanz.v_bro.element_classes.persistence.xml.model.XmlElementLutInfo;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import com.tschanz.v_bro.versions.domain.model.VersionData;
import com.tschanz.v_bro.versions.domain.service.VersionService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@RequiredArgsConstructor
public class XmlVersionService implements VersionService {
    @NonNull private final XmlRepoService repoService;
    @NonNull private final VersionParser parser;


    @Override
    public List<VersionData> readVersionTimeline(String elementClass, String elementId) throws RepoException {
        XmlElementLutInfo elementLut = this.repoService.getElementLut().get(elementId);
        if (elementLut == null) {
            throw new IllegalArgumentException("element id not found");
        }

        InputStream xmlFileStream = this.repoService.getNewXmlFileStream(elementLut.getStartBytePos(), elementLut.getEndBytePos());
        Collection<VersionData> versions = this.parser.readVersions(
            xmlFileStream,
            elementClass,
            elementId
        );

        return new ArrayList<>(versions);
    }
}
