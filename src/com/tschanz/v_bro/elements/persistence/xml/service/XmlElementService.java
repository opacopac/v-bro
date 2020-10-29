package com.tschanz.v_bro.elements.persistence.xml.service;

import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;

import java.util.*;


public class XmlElementService implements ElementService {
    private final XmlRepoService repoService;
    private final ElementParser elementParser;


    public XmlElementService(
        XmlRepoService repoService,
        ElementParser elementParser
    ) {
        this.repoService = repoService;
        this.elementParser = elementParser;
    }


    @Override
    public Collection<ElementData> readElements(String elementClass, Collection<String> fieldNames) throws RepoException {
        if (!this.repoService.isConnected()) {
            throw new RepoException("Repo not connected!");
        }

        Collection<ElementData> elementDataList = this.elementParser.readElementList(
            this.repoService,
            elementClass,
            fieldNames
        );

        return new ArrayList<>(elementDataList);
    }
}
