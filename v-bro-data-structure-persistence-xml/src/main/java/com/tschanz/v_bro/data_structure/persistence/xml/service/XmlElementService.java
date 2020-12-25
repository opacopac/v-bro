package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.service.ElementService;
import com.tschanz.v_bro.data_structure.persistence.xml.model.XmlElementLutInfo;
import com.tschanz.v_bro.data_structure.persistence.xml.stax_parser.ElementParser;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class XmlElementService implements ElementService {
    private final XmlRepoService repoService;
    private final ElementParser elementParser;


    @Override
    public List<ElementData> readElements(
        @NonNull ElementClass elementClass,
        @NonNull List<Denomination> denominationFields,
        @NonNull String query,
        int maxResults
    ) throws RepoException {
        var elementLuts = this.repoService.getElementLut().values()
            .stream()
            .filter(element -> elementClass.getName().equals(element.getName()))
            .sorted(Comparator.comparingInt(XmlElementLutInfo::getStartBytePos))
            .collect(Collectors.toList());

        var minBytePos = elementLuts.get(0).getStartBytePos();
        var maxBytePos = elementLuts.get(elementLuts.size() - 1).getEndBytePos();
        var xmlFileStream = this.repoService.getNewXmlFileStream(minBytePos, maxBytePos);
        var denominationFieldNames = denominationFields.stream().map(Denomination::getName).collect(Collectors.toList());

        var elementDataList = this.elementParser.readElements(
            xmlFileStream,
            elementClass,
            denominationFieldNames,
            query,
            maxResults
        );

        return elementDataList;
    }
}
