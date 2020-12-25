package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.ElementClassService;
import com.tschanz.v_bro.data_structure.persistence.xml.model.XmlElementLutInfo;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class XmlElementClassService implements ElementClassService {
    private final XmlRepoService repoService;


    @Override
    public List<ElementClass> readAllElementClasses() throws RepoException {
        return this.repoService.getElementLut().values()
            .stream()
            .map(XmlElementLutInfo::getName)
            .distinct()
            .sorted()
            .map(ElementClass::new)
            .collect(Collectors.toList());
    }
}
