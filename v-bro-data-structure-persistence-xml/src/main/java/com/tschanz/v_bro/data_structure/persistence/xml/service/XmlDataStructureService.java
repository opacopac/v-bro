package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.idref_parser.XmlIdElementPosInfo;
import com.tschanz.v_bro.repo.persistence.xml.idref_parser.XmlIdRefParser;
import com.tschanz.v_bro.repo.persistence.xml.idref_parser.XmlIdRefPosInfo;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlFieldInfo;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeInfo;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoConnectionService;
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
    private List<XmlIdElementPosInfo> elementPositionList;
    private Map<String, XmlIdElementPosInfo> elementPositionMap;
    private Map<String, List<XmlIdRefPosInfo>> elementRefPositionMap;


    public Map<String, XmlIdElementPosInfo> getElementLut() {
        return this.elementPositionMap;
    }


    public Map<String, List<XmlIdRefPosInfo>> getElementRefLut() {
        return this.elementRefPositionMap;
    }


    @SneakyThrows
    public XmlIdElementPosInfo getPosInfoByPos(int position) {
        if (this.elementPositionList == null) {
            this.readElementLut();
        }

        for (var pos : this.elementPositionList) {
            if (position >= pos.getStartBytePos() && position <= pos.getEndBytePos()) {
                return pos;
            }
        }

        throw new IllegalArgumentException(String.format("no pos info found with position %d ", position));
    }


    @SneakyThrows
    public InputStream getElementClassInputStream(String elementClassName) {
        var elementLuts = this.getElementLut().values()
            .stream()
            .filter(element -> elementClassName.equals(element.getName()))
            .sorted(Comparator.comparingInt(XmlIdElementPosInfo::getStartBytePos))
            .collect(Collectors.toList());

        return this.xmlRepoConnectionService.getNewXmlFileStream(elementLuts.get(0).getStartBytePos(), elementLuts.get(elementLuts.size() - 1).getEndBytePos());
    }


    public InputStream getElementInputStream(String elementId) throws RepoException {
        var elementLutInfo = this.getElementLut().get(elementId);
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


    public static boolean isId(String value) {
        if (value == null) {
            return false;
        }

        return (value.startsWith(XmlDataStructureService.ID_VALUE_PREFIX_1) || value.startsWith(XmlDataStructureService.ID_VALUE_PREFIX_2));
    }


    public void readElementLut() throws RepoException {
        var xmlFileStream = this.xmlRepoConnectionService.getNewXmlFileStream();
        var parser = new XmlIdRefParser(
            xmlFileStream,
            ID_ATTRIBUTE_NAME,
            List.of(ID_VALUE_PREFIX_1, ID_VALUE_PREFIX_2)
        );
        parser.parse();
        this.elementPositionList = parser.getIdElementPositions();

        this.elementPositionMap = new HashMap<>();
        this.elementPositionList.forEach(element -> this.elementPositionMap.put(element.getElementId(), element));

        var idRefs = parser.getIdRefPositions();
        this.elementRefPositionMap = new HashMap<>();
        for (var idRef : idRefs) {
            if (this.elementRefPositionMap.containsKey(idRef.getIdRef())) {
                this.elementRefPositionMap.get(idRef.getIdRef()).add(idRef);
            } else {
                List<XmlIdRefPosInfo> idRefList = new ArrayList<>();
                idRefList.add(idRef);
                this.elementRefPositionMap.put(idRef.getIdRef(), idRefList);
            }
        }
    }
}
