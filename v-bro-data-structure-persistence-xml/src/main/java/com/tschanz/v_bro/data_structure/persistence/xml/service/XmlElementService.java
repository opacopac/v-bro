package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.service.ElementService;
import com.tschanz.v_bro.data_structure.persistence.xml.stax_parser.ElementParser;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;


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
        var xmlFileStream = this.repoService.getElementClassInputStream(elementClass.getName());

        var elementDataList = this.elementParser.readElements(
            xmlFileStream,
            elementClass,
            denominationFields,
            query,
            maxResults
        );

        return elementDataList;
    }
}
