package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.idref_parser.XmlIdElementPosInfo;
import com.tschanz.v_bro.repo.persistence.xml.idref_parser.XmlIdRefParser;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlFieldInfo;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeInfo;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final XmlRepoService xmlRepoService;

    private Map<String, XmlIdElementPosInfo> elementStructureMap;


    public Map<String, XmlIdElementPosInfo> getElementLut() throws RepoException {
        if (this.elementStructureMap == null) {
            this.readElementLut();
        }

        return this.elementStructureMap;
    }


    public InputStream getElementClassInputStream(String elementClassName) throws RepoException {
        var elementLuts = this.getElementLut().values()
            .stream()
            .filter(element -> elementClassName.equals(element.getName()))
            .sorted(Comparator.comparingInt(XmlIdElementPosInfo::getStartBytePos))
            .collect(Collectors.toList());

        return this.xmlRepoService.getNewXmlFileStream(elementLuts.get(0).getStartBytePos(), elementLuts.get(elementLuts.size() - 1).getEndBytePos());
    }


    public InputStream getElementInputStream(String elementId) throws RepoException {
        var elementLutInfo = this.getElementLut().get(elementId);
        if (elementLutInfo == null) {
            throw new IllegalArgumentException(String.format("element id '%s' not found in xml lookup table", elementId));
        }

        return this.xmlRepoService.getNewXmlFileStream(elementLutInfo.getStartBytePos(), elementLutInfo.getEndBytePos());
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


    public static boolean isId(String value) {
        if (value == null) {
            return false;
        }

        return (value.startsWith(XmlDataStructureService.ID_VALUE_PREFIX_1) || value.startsWith(XmlDataStructureService.ID_VALUE_PREFIX_2));
    }


    private void readElementLut() throws RepoException {
        var xmlFileStream = this.xmlRepoService.getNewXmlFileStream();
        var parser = new XmlIdRefParser(
            xmlFileStream,
            ID_ATTRIBUTE_NAME,
            List.of(ID_VALUE_PREFIX_1, ID_VALUE_PREFIX_2)
        );
        parser.parse();
        var idElements = parser.getIdElementPositions();
        var idRefs = parser.getIdRefPositions();

        this.elementStructureMap = new HashMap<>();
        idElements.forEach(element -> this.elementStructureMap.put(element.getElementId(), element));
    }
}
