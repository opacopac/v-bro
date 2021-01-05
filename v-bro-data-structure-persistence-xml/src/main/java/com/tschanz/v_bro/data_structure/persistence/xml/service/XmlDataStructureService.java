package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.data_structure.persistence.xml.model.XmlStructureData;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.idref_parser.XmlIdElementPosInfo;
import com.tschanz.v_bro.repo.persistence.xml.idref_parser.XmlIdRefParser;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlFieldInfo;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeInfo;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoConnectionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class XmlDataStructureService {
    // TODO => config file
    public static final String ID_ATTRIBUTE_NAME = "id";
    public static final String ID_VALUE_PREFIX_1 = "idd__";
    public static final String ID_VALUE_PREFIX_2 = "ids__";
    public static final String VERSION_NODE_NAME = "version";
    public static final String VERSION_VON_ATTRIBUTE_NAME = "gueltigVon";
    public static final String VERSION_BIS_ATTRIBUTE_NAME = "gueltigBis";
    private final XmlRepoConnectionService xmlRepoConnectionService;
    private Map<String, XmlIdElementPosInfo> elementPositionMap;
    @Getter private Map<String, XmlStructureData> xmlStructureMap;


    public static boolean isId(String value) {
        if (value == null) {
            return false;
        }

        return (value.startsWith(XmlDataStructureService.ID_VALUE_PREFIX_1) || value.startsWith(XmlDataStructureService.ID_VALUE_PREFIX_2));
    }


    @SneakyThrows
    public InputStream getElementClassInputStream(String elementClassName) {
        var elementLuts = this.getPosInfosByElementClass(elementClassName);

        return this.xmlRepoConnectionService.getNewXmlFileStream(elementLuts.get(0).getStartBytePos(), elementLuts.get(elementLuts.size() - 1).getEndBytePos());
    }


    @SneakyThrows
    public InputStream getElementInputStream(String elementId) {
        var elementLutInfo = this.elementPositionMap.get(elementId);
        if (elementLutInfo == null) {
            throw new IllegalArgumentException(String.format("element id '%s' not found in xml lookup table", elementId));
        }

        return this.xmlRepoConnectionService.getNewXmlFileStream(elementLutInfo.getStartBytePos(), elementLutInfo.getEndBytePos());
    }


    public List<XmlNodeInfo> getVersionNodes(XmlNodeInfo elementNode) {
        return elementNode.getChildNodes()
            .stream()
            .filter(node -> node.getName().equals(XmlDataStructureService.VERSION_NODE_NAME))
            .collect(Collectors.toList());
    }


    public String getId(XmlNodeInfo node) {
        return node.getFields()
            .stream()
            .filter(XmlFieldInfo::isAttribute)
            .filter(f -> f.getName().equals(XmlDataStructureService.ID_ATTRIBUTE_NAME))
            .map(XmlFieldInfo::getValue)
            .findFirst()
            .orElse(null);
    }


    public void readElementLut() throws RepoException {
        var xmlFileStream = this.xmlRepoConnectionService.getNewXmlFileStream();
        var parser = new XmlIdRefParser(
            xmlFileStream,
            ID_ATTRIBUTE_NAME,
            List.of(ID_VALUE_PREFIX_1, ID_VALUE_PREFIX_2)
        );
        parser.parse();
        List<XmlIdElementPosInfo> elementPositionList = parser.getIdElementPositions();

        this.elementPositionMap = new HashMap<>();
        elementPositionList.forEach(element -> this.elementPositionMap.put(element.getElementId(), element));

        this.xmlStructureMap = new HashMap<>();
        // add fwd refs
        for (var elementPosInfo: this.elementPositionMap.values()) {
            var dependencyData = new XmlStructureData(elementPosInfo.getName(), elementPosInfo.getElementId());
            for (var refId: elementPosInfo.getIdRefs()) {
                var refElement = this.elementPositionMap.get(refId);
                if (refElement != null) {
                    dependencyData.getFwdElementIds().add(refId);
                }
            }
            this.xmlStructureMap.put(elementPosInfo.getElementId(), dependencyData);
        }

        // add bwd refs
        for (var element: this.xmlStructureMap.values()) {
            for (var fwdElementId: element.getFwdElementIds()) {
                var fwdElement = this.xmlStructureMap.get(fwdElementId);
                fwdElement.getBwdElementIds().add(element.getElementId()); // remark: allowing duplicate entries due to performance reasons
            }
        }
    }


    private List<XmlIdElementPosInfo> getPosInfosByElementClass(String elementClassName) {
        return this.elementPositionMap.values()
            .stream()
            .filter(element -> elementClassName.equals(element.getName()))
            .sorted(Comparator.comparingInt(XmlIdElementPosInfo::getStartBytePos))
            .collect(Collectors.toList());
    }
}
