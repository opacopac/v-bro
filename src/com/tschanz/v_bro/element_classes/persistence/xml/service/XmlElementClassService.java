package com.tschanz.v_bro.element_classes.persistence.xml.service;

import com.tschanz.v_bro.element_classes.domain.model.Denomination;
import com.tschanz.v_bro.element_classes.domain.model.ElementClass;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.element_classes.persistence.xml.model.XmlElementClass;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;

import java.util.*;


public class XmlElementClassService implements ElementClassService {
    private final XmlRepoService repoService;
    private final ElementClassParser elementClassParser;
    private final Map<String, List<Denomination>> elementDenominations = new HashMap<>();


    public XmlElementClassService(
        XmlRepoService repoService,
        ElementClassParser elementClassParser
    ) {
        this.repoService = repoService;
        this.elementClassParser = elementClassParser;
    }


    @Override
    public Collection<ElementClass> readElementClasses() throws RepoException {
        if (!this.repoService.isConnected()) {
            throw new RepoException("Repo not connected");
        }

        Collection<XmlElementClass> elementStructureList = this.elementClassParser.readElementStructure(this.repoService);

        this.elementDenominations.clear();
        elementStructureList.forEach(elementClass -> this.elementDenominations.put(elementClass.getName(), elementClass.getNameFields()));

        return new ArrayList<>(elementStructureList);
    }


    @Override
    public List<Denomination> readDenominations(String elementName) throws RepoException {
        return this.elementDenominations.get(elementName);
    }
}
