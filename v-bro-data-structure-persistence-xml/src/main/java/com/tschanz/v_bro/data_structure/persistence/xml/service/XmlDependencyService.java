package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.*;
import com.tschanz.v_bro.data_structure.domain.service.DependencyService;
import com.tschanz.v_bro.data_structure.persistence.xml.model.XmlStructureData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class XmlDependencyService implements DependencyService {
    private final XmlDataStructureService xmlDataStructureService;
    private final XmlElementService elementService;
    private final XmlVersionService versionService;
    private final XmlVersionAggregateService versionAggregateService;


    @Override
    public List<Dependency> readFwdDependencies(
        @NonNull VersionData version,
        @NonNull LocalDate minGueltigVon,
        @NonNull LocalDate maxGueltigBis,
        @NonNull Pflegestatus minPflegestatus,
        ElementClass elementClassFilter,
        @NonNull List<Denomination> denominations,
        int maxResults
    ) {
        var xmlStructureMap = this.xmlDataStructureService.getXmlStructureMap();
        var versionAggregate = this.versionAggregateService.readVersionAggregate(version);
        List<Dependency> fwdDependencies = new ArrayList<>();
        this.addNodeDependencies(fwdDependencies, versionAggregate.getRootNode(), xmlStructureMap, minGueltigVon, maxGueltigBis, minPflegestatus, elementClassFilter, denominations, maxResults);

        return fwdDependencies;
    }


    @Override
    public List<Dependency> readBwdDependencies(
        @NonNull ElementData element,
        @NonNull LocalDate minGueltigVon,
        @NonNull LocalDate maxGueltigBis,
        @NonNull Pflegestatus minPflegestatus,
        ElementClass elementClassFilter,
        @NonNull List<Denomination> denominations,
        int maxResults
    ) {
        var elementInfo = this.xmlDataStructureService.getXmlStructureMap().get(element.getId());
        var bwdElementIds = elementInfo.getBwdElementIds().stream().distinct().collect(Collectors.toList());
        List<Dependency> dependencies = new ArrayList<>();
        for (var bwdElementId: bwdElementIds) {
            if (dependencies.size() >= maxResults) {
                break;
            }
            var bwdElementInfo = this.xmlDataStructureService.getXmlStructureMap().get(bwdElementId);
            if (elementClassFilter != null && !bwdElementInfo.getElementClass().equals(elementClassFilter.getName())) {
                continue;
            }
            var bwdElementClass = new ElementClass(bwdElementInfo.getElementClass());
            var bwdElement = this.elementService.readElement(bwdElementClass, denominations, bwdElementInfo.getElementId());
            var bwdVersions = this.versionService.readVersions(bwdElement, minGueltigVon, maxGueltigBis, minPflegestatus);
            var bwdDependency = new Dependency(bwdElementClass, bwdElement, bwdVersions);
            dependencies.add(bwdDependency);
        }

        return dependencies;
    }


    private void addNodeDependencies(
        List<Dependency> fwdDependencies,
        AggregateNode aggregateNode,
        Map<String, XmlStructureData> xmlStructureData,
        LocalDate minGueltigVon,
        LocalDate maxGueltigBis,
        Pflegestatus minPflegestatus,
        ElementClass elementClassFilter,
        List<Denomination> denominations,
        int maxResults
    ) {
        var fwdElements = aggregateNode.getFieldValues()
            .stream()
            .filter(keyValue -> !keyValue.key.equals(XmlDataStructureService.ID_ATTRIBUTE_NAME)) // filter own element/version id
            .filter(keyValue -> XmlDataStructureService.isId(keyValue.value))
            .flatMap(keyValue -> List.of(keyValue.value.split(" ")).stream())
            .map(xmlStructureData::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        for (var fwdElement: fwdElements) {
            if (fwdDependencies.size() >= maxResults) {
                return;
            }
            if (elementClassFilter != null && !fwdElement.getElementClass().equals(elementClassFilter.getName())) {
                continue;
            }
            var elementClass = new ElementClass(fwdElement.getElementClass());
            var element = new ElementData(elementClass, fwdElement.getElementId(), Collections.emptyList());
            var versions = this.versionService.readVersions(element, minGueltigVon, maxGueltigBis, minPflegestatus);
            var fwdDependency = new Dependency(elementClass, element, versions);
            fwdDependencies.add(fwdDependency);
        }

        for (AggregateNode childNode: aggregateNode.getChildNodes()) {
            this.addNodeDependencies(fwdDependencies, childNode, xmlStructureData, minGueltigVon, maxGueltigBis, minPflegestatus, elementClassFilter, denominations, maxResults);
        }
    }
}
