package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.ElementClassService;
import com.tschanz.v_bro.data_structure.persistence.xml.model.XmlElementLutInfo;
import com.tschanz.v_bro.data_structure.persistence.xml.stax_parser.DenominationsParser;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class XmlElementClassService implements ElementClassService {
    private final XmlRepoService repoService;
    private final DenominationsParser denominationsParser;


    @Override
    public List<ElementClass> readElementClasses() throws RepoException {
        return this.repoService.getElementLut().values()
            .stream()
            .map(XmlElementLutInfo::getName)
            .distinct()
            .sorted()
            .map(ElementClass::new)
            .collect(Collectors.toList());
    }


    @Override
    public List<Denomination> readDenominations(String elementClass) throws RepoException {
        var elementLutInfo = this.repoService.getElementLut().values()
            .stream()
            .filter(element -> elementClass.equals(element.getName()))
            .findFirst() // TODO: better: find all and create set
            .orElseThrow(() -> new RepoException(String.format("no element with name '%s' found", elementClass)));
        var xmlFileStream = this.repoService.getNewXmlFileStream(elementLutInfo.getStartBytePos(), elementLutInfo.getEndBytePos());
        var denominations = this.denominationsParser.readDenominations(xmlFileStream, elementClass);

        return denominations;
    }
}
