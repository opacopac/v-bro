package com.tschanz.v_bro.elements.xml.service;

import com.tschanz.v_bro.elements.domain.model.ElementClass;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.elements.domain.model.Denomination;
import com.tschanz.v_bro.elements.xml.model.XmlElementClass;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.xml.service.XmlRepoService;

import java.util.*;


public class XmlElementService implements ElementService {
    private final XmlRepoService repoService;
    private final ElementClassParser elementClassParser;
    private final ElementParser elementParser;
    private final Map<String, List<Denomination>> nameFields = new HashMap<>();


    public XmlElementService(
        XmlRepoService repoService,
        ElementClassParser elementClassParser,
        ElementParser elementParser
    ) {
        this.repoService = repoService;
        this.elementClassParser = elementClassParser;
        this.elementParser = elementParser;
    }


    @Override
    public Collection<ElementClass> readElementClasses() throws RepoException {
        if (!this.repoService.isConnected()) {
            throw new RepoException("Repo not connected");
        }

        Collection<XmlElementClass> elementStructureList = this.elementClassParser.readElementStructure(this.repoService);

        // update name field map
        this.nameFields.clear();
        elementStructureList.forEach(elementClass -> this.nameFields.put(elementClass.getName(), elementClass.getNameFields()));

        return new ArrayList<>(elementStructureList);
    }


    @Override
    public List<Denomination> readDenominations(String elementName) throws RepoException {
        return this.nameFields.get(elementName);
    }


    @Override
    public Collection<ElementData> readElements(String elementName, Collection<String> fieldNames) throws RepoException {
        if (!this.repoService.isConnected()) {
            throw new RepoException("Repo not connected!");
        }

        Collection<ElementData> elementDataList = this.elementParser.readElementList(
            this.repoService,
            elementName,
            fieldNames
        );

        return new ArrayList<>(elementDataList);
    }
}
