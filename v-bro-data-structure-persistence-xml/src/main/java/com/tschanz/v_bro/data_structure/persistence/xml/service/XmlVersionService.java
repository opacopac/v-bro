package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.VersionService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlFieldInfo;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeInfo;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeParser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class XmlVersionService implements VersionService {
    @NonNull private final XmlRepoService repoService;
    @NonNull private final XmlNodeParser xmlNodeParser;


    @Override
    public List<VersionData> readVersions(@NonNull ElementData element) throws RepoException {
        InputStream xmlFileStream = this.repoService.getElementInputStream(element.getId());
        this.xmlNodeParser.init(xmlFileStream, element.getElementClass().getName());
        var elementNode = this.xmlNodeParser.nextNode();
        var versionNodes = this.repoService.getVersionNodes(elementNode);

        if (versionNodes.size() > 0) {
            return versionNodes
                .stream()
                .map(versionNode -> this.createVersion(element, versionNode))
                .collect(Collectors.toList());
        } else {
            return List.of(VersionData.createEternal(element));
        }
    }


    private VersionData createVersion(ElementData element, XmlNodeInfo versionNode) {
        var gueltigVon = versionNode.getFields()
            .stream()
            .filter(XmlFieldInfo::isAttribute)
            .filter(field -> field.getName().equals(XmlRepoService.VERSION_VON_ATTRIBUTE_NAME))
            .map(field -> LocalDate.parse(field.getValue()))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);

        var gueltigBis = versionNode.getFields()
            .stream()
            .filter(XmlFieldInfo::isAttribute)
            .filter(field -> field.getName().equals(XmlRepoService.VERSION_BIS_ATTRIBUTE_NAME))
            .map(field -> LocalDate.parse(field.getValue()))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);

        return new VersionData(
            element,
            this.repoService.getId(versionNode),
            gueltigVon,
            gueltigBis,
            Pflegestatus.PRODUKTIV
        );
    }
}
