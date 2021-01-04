package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.domain.model.*;
import com.tschanz.v_bro.data_structure.domain.service.DependencyService;
import com.tschanz.v_bro.repo.persistence.xml.idref_parser.XmlIdElementPosInfo;
import com.tschanz.v_bro.repo.domain.model.RepoException;
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
        @NonNull Pflegestatus minPflegestatus
    ) {
        Map<String, XmlIdElementPosInfo> elementLutInfos = this.xmlDataStructureService.getElementLut();
        VersionAggregate versionAggregate = this.versionAggregateService.readVersionAggregate(version);

        return this.findNodeDependencies(versionAggregate.getRootNode(), elementLutInfos, minGueltigVon, maxGueltigBis, minPflegestatus);
    }


    @Override
    public List<Dependency> readBwdDependencies(
        @NonNull ElementData element,
        @NonNull LocalDate minGueltigVon,
        @NonNull LocalDate maxGueltigBis,
        @NonNull Pflegestatus minPflegestatus
    ) {
        var bwdElementInfos = this.findBwdElementInfos(element.getId());
        List<Dependency> dependencies = new ArrayList<>();
        for (var bwdElementInfo: bwdElementInfos) {
            var bwdElementClass = new ElementClass(bwdElementInfo.getName());
            var bwdElement = this.elementService.readElement(bwdElementClass, Collections.emptyList(), bwdElementInfo.getElementId());
            var bwdVersions = this.versionService.readVersions(bwdElement, minGueltigVon, maxGueltigBis, minPflegestatus);
            var bwdDependency = new Dependency(bwdElementClass, bwdElement, bwdVersions);
            dependencies.add(bwdDependency);
        }

        return dependencies;
    }


    private List<XmlIdElementPosInfo> findBwdElementInfos(String elementId) {
        var elementRefLut = this.xmlDataStructureService.getElementRefLut();
        if (elementRefLut.containsKey(elementId)) {
            var bwdRefs = elementRefLut.get(elementId);
            List<XmlIdElementPosInfo> bwdElementInfos = new ArrayList<>();
            for (var bwdRef : bwdRefs) {
                var bwdElementInfo = this.xmlDataStructureService.getPosInfoByPos(bwdRef.getStartBytePos());
                bwdElementInfos.add(bwdElementInfo);
            }
            return bwdElementInfos;
        } else {
            return Collections.emptyList();
        }
    }


    private List<Dependency> findNodeDependencies(
        AggregateNode aggregateNode,
        Map<String,XmlIdElementPosInfo> elementLutInfos,
        LocalDate minGueltigVon,
        LocalDate maxGueltigBis,
        Pflegestatus minPflegestatus
    ) {
        List<Dependency> fwdDependencies = new ArrayList<>();

        List<XmlIdElementPosInfo> fwdElements = aggregateNode.getFieldValues()
            .stream()
            .filter(keyValue -> !keyValue.key.equals(XmlDataStructureService.ID_ATTRIBUTE_NAME)) // filter own element/version id
            .filter(keyValue -> XmlDataStructureService.isId(keyValue.value))
            .flatMap(keyValue -> List.of(keyValue.value.split(" ")).stream())
            .map(elementLutInfos::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        for (XmlIdElementPosInfo fwdElement: fwdElements) {
            var elementClass = new ElementClass(fwdElement.getName());
            var element = new ElementData(elementClass, fwdElement.getElementId(), Collections.emptyList());
            var versions = this.versionService.readVersions(element, minGueltigVon, maxGueltigBis, minPflegestatus);
            var fwdDependency = new Dependency(elementClass, element, versions);
            fwdDependencies.add(fwdDependency);
        }

        for (AggregateNode childNode: aggregateNode.getChildNodes()) {
            fwdDependencies.addAll(this.findNodeDependencies(childNode, elementLutInfos, minGueltigVon, maxGueltigBis, minPflegestatus));
        }

        return fwdDependencies;
    }
}
