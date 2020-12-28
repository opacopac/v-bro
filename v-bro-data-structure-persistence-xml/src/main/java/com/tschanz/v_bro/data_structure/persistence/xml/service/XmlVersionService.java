package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.VersionService;
import com.tschanz.v_bro.repo.persistence.xml.parser.XmlIdElementPosInfo;
import com.tschanz.v_bro.data_structure.persistence.xml.stax_parser.VersionParser;
import com.tschanz.v_bro.repo.domain.model.RepoException;
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
    public List<VersionData> readVersions(@NonNull ElementData element) throws RepoException {
        XmlIdElementPosInfo elementLut = this.repoService.getElementLut().get(element.getId());
        if (elementLut == null) {
            throw new IllegalArgumentException("element id not found");
        }

        InputStream xmlFileStream = this.repoService.getElementInputStream(element.getId());
        Collection<VersionData> versions = this.parser.readVersions(
            xmlFileStream,
            element
        );

        return new ArrayList<>(versions);
    }
}
