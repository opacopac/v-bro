package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.DenominationService;
import com.tschanz.v_bro.data_structure.persistence.xml.stax_parser.DenominationsParser;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class XmlDenominationService implements DenominationService {
    private final XmlRepoService repoService;
    private final DenominationsParser denominationsParser;


    @Override
    public List<Denomination> readDenominations(@NonNull ElementClass elementClass) throws RepoException {
        var elementClassName = elementClass.getName();
        var elementLutInfo = this.repoService.getElementLut().values()
            .stream()
            .filter(element -> elementClassName.equals(element.getName()))
            .findFirst() // TODO: better: find all and create set
            .orElseThrow(() -> new RepoException(String.format("no element with name '%s' found", elementClassName)));
        var xmlFileStream = this.repoService.getNewXmlFileStream(elementLutInfo.getStartBytePos(), elementLutInfo.getEndBytePos());
        var denominations = this.denominationsParser.readDenominations(xmlFileStream, elementClassName);

        return denominations;
    }
}
