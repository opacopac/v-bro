package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.ElementClassService;
import com.tschanz.v_bro.repo.persistence.xml.idref_parser.XmlIdElementPosInfo;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class XmlElementClassService implements ElementClassService {
    private final XmlDataStructureService xmlDataStructureService;


    @Override
    public List<ElementClass> readAllElementClasses() throws RepoException {
        return this.xmlDataStructureService.getElementLut().values()
            .stream()
            .map(XmlIdElementPosInfo::getName)
            .distinct()
            .sorted()
            .map(ElementClass::new)
            .collect(Collectors.toList());
    }
}
