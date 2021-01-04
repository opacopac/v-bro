package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.model.Pflegestatus;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.VersionService;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlFieldInfo;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeInfo;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeParser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class XmlVersionService implements VersionService {
    @NonNull private final XmlDataStructureService xmlDataStructureService;
    @NonNull private final XmlNodeParser xmlNodeParser;


    @Override
    @SneakyThrows
    public List<VersionData> readVersions(
        @NonNull ElementData element,
        @NonNull LocalDate timelineVon,
        @NonNull LocalDate timelineBis,
        @NonNull Pflegestatus minPflegestatus
    ) {
        InputStream xmlFileStream = this.xmlDataStructureService.getElementInputStream(element.getId());
        this.xmlNodeParser.init(xmlFileStream, element.getElementClass().getName());
        var elementNode = this.xmlNodeParser.nextNode();
        var versionNodes = this.xmlDataStructureService.getVersionNodes(elementNode);

        if (versionNodes.size() > 0) {
            return versionNodes
                .stream()
                .map(versionNode -> this.createVersion(element, versionNode))
                .filter(v -> v.getGueltigBis().isAfter(timelineVon) || v.getGueltigBis().isEqual(timelineVon))
                .filter(v -> v.getGueltigVon().isBefore(timelineBis) || v.getGueltigVon().isEqual(timelineBis))
                .collect(Collectors.toList());
        } else {
            return List.of(VersionData.createEternal(element));
        }
    }


    private VersionData createVersion(ElementData element, XmlNodeInfo versionNode) {
        var gueltigVon = versionNode.getFields()
            .stream()
            .filter(XmlFieldInfo::isAttribute)
            .filter(field -> field.getName().equals(XmlDataStructureService.VERSION_VON_ATTRIBUTE_NAME))
            .map(field -> LocalDate.parse(field.getValue()))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);

        var gueltigBis = versionNode.getFields()
            .stream()
            .filter(XmlFieldInfo::isAttribute)
            .filter(field -> field.getName().equals(XmlDataStructureService.VERSION_BIS_ATTRIBUTE_NAME))
            .map(field -> LocalDate.parse(field.getValue()))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);

        return new VersionData(
            element,
            this.xmlDataStructureService.getId(versionNode),
            gueltigVon,
            gueltigBis,
            Pflegestatus.PRODUKTIV
        );
    }
}
